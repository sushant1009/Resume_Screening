import { useNavigate } from "react-router-dom";

const useLogout = () => {
  const navigate = useNavigate();

  const logout = () => {
    // Clear JWT and role from sessionStorage
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("role");

    // Redirect to login page
    navigate("/login");
  };

  return logout;
};

export default useLogout;
