import { createContext } from "react";

interface AuthContext {
  user: number | null;
  setUser: (user: number | null) => void;
}

export const AuthContext = createContext<AuthContext>({
  user: null,
  setUser: () => {},
});
