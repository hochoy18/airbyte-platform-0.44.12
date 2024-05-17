import styles from "./BuilderLogo.module.scss";

export const BuilderLogo = ({ title }: { title?: string }): JSX.Element => (
  <svg
    width="18"
    height="18"
    viewBox="0 0 250 250"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    className={styles.svg}
  >
    {title && <title>{title}</title>}
    <g clip-path="url(#clip0_1789_6842)">
      <path
        d="M42.0027 35L27 49.9996L42.0027 79.9987L64.5067 87.4985L90.7613 113.748L105.764 98.7481L79.5093 72.4989L72.008 49.9996L42.0027 35ZM177.525 35.0732C165.084 34.5814 152.495 39.0693 142.992 48.5641C130.72 60.8342 129.621 75.5919 135.725 89.4759L48.5956 176.588C39.8041 185.377 39.8041 199.613 48.5956 208.403C57.3872 217.193 71.6261 217.193 80.4177 208.403L167.547 121.291C181.433 127.392 196.196 126.286 208.468 114.011C221.137 101.344 224.894 83.1852 219.808 67.2256L192.015 95.0275L168.002 89.0219L161.995 65.0138L189.802 37.2265C185.81 35.9572 181.672 35.2372 177.525 35.0732ZM165.936 144.772L137.703 172.999L173.847 209.15C181.649 216.95 194.301 216.95 202.095 209.15C209.896 201.35 209.896 188.709 202.095 180.909L165.936 144.772ZM64.5067 184.996C68.6474 184.996 72.008 188.355 72.008 192.495C72.008 196.635 68.6474 199.995 64.5067 199.995C60.3659 199.995 57.0053 196.635 57.0053 192.495C57.0053 188.355 60.3659 184.996 64.5067 184.996Z"
        fill="currentColor"
      />
    </g>
    <defs>
      <clipPath id="clip0_1789_6842">
        <rect width="250" height="250" fill="white" />
      </clipPath>
    </defs>
  </svg>
);
