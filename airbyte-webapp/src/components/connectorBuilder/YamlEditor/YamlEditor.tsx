import { useMonaco } from "@monaco-editor/react";
import { load, YAMLException } from "js-yaml";
import debounce from "lodash/debounce";
import isEqual from "lodash/isEqual";
import { editor } from "monaco-editor/esm/vs/editor/editor.api";
import { useEffect, useMemo, useRef, useState } from "react";
import React from "react";
import { useFormContext } from "react-hook-form";

import { CodeEditor } from "components/ui/CodeEditor";
import { FlexContainer, FlexItem } from "components/ui/Flex";

import { ConnectorManifest } from "core/request/ConnectorManifest";
import { Action, Namespace } from "core/services/analytics";
import { useAnalyticsService } from "core/services/analytics";
import { useConfirmationModalService } from "hooks/services/ConfirmationModal";
import { useConnectorBuilderFormState } from "services/connectorBuilder/ConnectorBuilderStateService";

import { NameInput } from "./NameInput";
import styles from "./YamlEditor.module.scss";
import { SavingIndicator } from "../Builder/SavingIndicator";
import { UiYamlToggleButton } from "../Builder/UiYamlToggleButton";
import { DownloadYamlButton } from "../DownloadYamlButton";
import { PublishButton } from "../PublishButton";
import { convertToManifest } from "../types";
import { useManifestToBuilderForm } from "../useManifestToBuilderForm";

interface YamlEditorProps {
  toggleYamlEditor: () => void;
}

export const YamlEditor: React.FC<YamlEditorProps> = ({ toggleYamlEditor }) => {
  const analyticsService = useAnalyticsService();
  const { setValue } = useFormContext();
  const { openConfirmationModal, closeConfirmationModal } = useConfirmationModalService();
  const yamlEditorRef = useRef<editor.IStandaloneCodeEditor>();
  const {
    yamlManifest,
    yamlIsValid,
    jsonManifest,
    builderFormValues,
    setYamlEditorIsMounted,
    setYamlIsValid,
    setJsonManifest,
  } = useConnectorBuilderFormState();
  const [yamlValue, setYamlValue] = useState(yamlManifest);
  const { convertToBuilderFormValues } = useManifestToBuilderForm();

  // debounce the setJsonManifest calls so that it doesnt result in a network call for every keystroke
  const debouncedSetJsonManifest = useMemo(() => debounce(setJsonManifest, 200), [setJsonManifest]);
  const initialLoad = useRef(true);

  const monaco = useMonaco();

  useEffect(() => {
    if (monaco && yamlEditorRef.current && yamlValue) {
      const errOwner = "yaml";
      const yamlEditorModel = yamlEditorRef.current.getModel();

      try {
        const json = load(yamlValue) as ConnectorManifest;
        setYamlIsValid(true);
        // skip setting the manifest on the first load as it just got passed in and is synced already
        if (initialLoad.current) {
          initialLoad.current = false;
        } else {
          debouncedSetJsonManifest(json);
        }

        // clear editor error markers
        if (yamlEditorModel) {
          monaco.editor.setModelMarkers(yamlEditorModel, errOwner, []);
        }
      } catch (err) {
        if (err instanceof YAMLException) {
          setYamlIsValid(false);
          const mark = err.mark;

          // set editor error markers
          if (yamlEditorModel) {
            monaco.editor.setModelMarkers(yamlEditorModel, errOwner, [
              {
                startLineNumber: mark.line + 1,
                startColumn: mark.column + 1,
                endLineNumber: mark.line + 1,
                endColumn: mark.column + 2,
                message: err.message,
                severity: monaco.MarkerSeverity.Error,
              },
            ]);
          }
        }
      }
    }
  }, [yamlValue, monaco, debouncedSetJsonManifest, setYamlIsValid]);

  const yamlIsDirty = useMemo(() => {
    return !isEqual(convertToManifest(builderFormValues), jsonManifest);
  }, [jsonManifest, builderFormValues]);

  const handleToggleYamlEditor = async () => {
    if (yamlIsDirty) {
      try {
        const convertedFormValues = await convertToBuilderFormValues(
          jsonManifest,
          builderFormValues.global.connectorName
        );
        Object.entries(convertedFormValues).forEach(([key, value]) => {
          setValue(key, value);
        });
        toggleYamlEditor();
      } catch (e) {
        openConfirmationModal({
          text: "connectorBuilder.toggleModal.text",
          textValues: { error: e.message as string },
          title: "connectorBuilder.toggleModal.title",
          submitButtonText: "connectorBuilder.toggleModal.submitButton",
          onSubmit: () => {
            setYamlIsValid(true);
            toggleYamlEditor();
            closeConfirmationModal();
            analyticsService.track(Namespace.CONNECTOR_BUILDER, Action.DISCARD_YAML_CHANGES, {
              actionDescription: "YAML changes were discarded due to failure when converting from YAML to UI",
            });
          },
        });
        analyticsService.track(Namespace.CONNECTOR_BUILDER, Action.YAML_TO_UI_CONVERSION_FAILURE, {
          actionDescription: "Failure occured when converting from YAML to UI",
          error_message: e.message,
        });
      }
    } else {
      setYamlIsValid(true);
      toggleYamlEditor();
    }
  };

  return (
    <div className={styles.container}>
      <FlexContainer alignItems="center" className={styles.control}>
        <UiYamlToggleButton yamlSelected onClick={handleToggleYamlEditor} className={styles.toggleButton} />
        <NameInput />
        <SavingIndicator />
        <FlexItem grow>
          <FlexContainer justifyContent="flex-end">
            <DownloadYamlButton yaml={yamlValue} yamlIsValid={yamlIsValid} />
            <PublishButton />
          </FlexContainer>
        </FlexItem>
      </FlexContainer>
      <div className={styles.editorContainer}>
        <CodeEditor
          value={yamlValue}
          language="yaml"
          theme="airbyte-light"
          onChange={(value) => setYamlValue(value ?? "")}
          lineNumberCharacterWidth={6}
          onMount={(editor) => {
            setYamlEditorIsMounted(true);
            yamlEditorRef.current = editor;
          }}
        />
      </div>
    </div>
  );
};
