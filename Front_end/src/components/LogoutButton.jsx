import React from "react";
import useLogout from "../hooks/useLogout"; // adjust path

const LogoutButton = () => {
  const logout = useLogout();

  return (
    <button
      onClick={logout}
      style={{
        marginLeft: "20px",
        padding: "5px 10px",
        cursor: "pointer"
      }}
    >
      Logout
    </button>
  );
};

export default LogoutButton;
