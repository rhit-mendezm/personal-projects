import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./App.css";
import { useAuth } from "./services/useAuth";
import { AuthContext } from "./context/AuthContext";
import Home from "./pages/home";
import Login from "./pages/login";
import Register from "./pages/register";
import UpdateAccount from "./pages/updateAccount";
import Post from "./pages/post";
import Saved from "./pages/saved";
import OrganizationsPage from "./pages/organizations";
import MyOrgsPage from "./pages/myOrgs";

function App() {
  const { user, setUser } = useAuth();

  const router = createBrowserRouter([
    { path: "/", element: <Home /> },
    { path: "/login", element: <Login /> },
    { path: "/register", element: <Register /> },
    { path: "/updateAccount", element: <UpdateAccount /> },
    { path: "/post/:id", element: <Post /> },
    { path: "/saved", element: <Saved /> },
    { path: "/organizations", element: <OrganizationsPage />},
    { path: "/myOrgs", element: <MyOrgsPage />}
  ]);

  return (
    <AuthContext.Provider value={{ user, setUser }}>
      <RouterProvider router={router} />
    </AuthContext.Provider>
  );
}

export default App;
