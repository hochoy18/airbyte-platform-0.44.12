const BuilderIcon = ({ color = "currentColor" }: { color?: string }): JSX.Element => (
  <svg width="18" height="19" viewBox="0 0 18 19" fill="none">
    <path
      d="M2.33004 0.924192C2.95409 0.701566 3.62851 0.660647 4.2749 0.806193C4.92129 0.951739 5.51308 1.27777 5.98148 1.74638C6.44989 2.215 6.77565 2.80694 6.92091 3.45339C7.06617 4.09984 7.02495 4.77424 6.80204 5.39819L17.647 16.2432L15.525 18.3642L4.68004 7.52019C4.05599 7.74282 3.38157 7.78374 2.73519 7.63819C2.0888 7.49264 1.497 7.16661 1.0286 6.698C0.560199 6.22939 0.234432 5.63745 0.089175 4.991C-0.0560821 4.34454 -0.014862 3.67014 0.208043 3.04619L2.44404 5.28319C2.58241 5.42646 2.74793 5.54073 2.93094 5.61934C3.11394 5.69796 3.31077 5.73934 3.50994 5.74107C3.70911 5.7428 3.90663 5.70485 4.09097 5.62942C4.27532 5.554 4.4428 5.44262 4.58364 5.30178C4.72447 5.16095 4.83585 4.99347 4.91128 4.80912C4.9867 4.62478 5.02465 4.42726 5.02292 4.22809C5.02119 4.02892 4.97981 3.83209 4.9012 3.64909C4.82258 3.46608 4.70831 3.30056 4.56504 3.16219L2.32904 0.923192L2.33004 0.924192ZM12.697 2.80819L15.879 1.04019L17.293 2.45419L15.525 5.63619L13.757 5.99019L11.637 8.11119L10.222 6.69719L12.343 4.57619L12.697 2.80819ZM5.62604 10.5862L7.74704 12.7082L2.79704 17.6582C2.52408 17.9286 2.15776 18.084 1.77362 18.0922C1.38949 18.1005 1.01683 17.961 0.732497 17.7026C0.448164 17.4442 0.27384 17.0865 0.245474 16.7033C0.217108 16.3201 0.336863 15.9407 0.580043 15.6432L0.677043 15.5362L5.62604 10.5862Z"
      fill={color}
    />
  </svg>
);

export default BuilderIcon;
