import React from "react";
import "./AuroraBackground.css";

export const AuroraBackground = ({ children }) => {
  return (
    <div className="aurora-wrapper">
      <div className="aurora-gradient-one"></div>
      <div className="aurora-gradient-two"></div>
      <div className="aurora-gradient-three"></div>
      <div className="aurora-content">{children}</div>
    </div>
  );
};
