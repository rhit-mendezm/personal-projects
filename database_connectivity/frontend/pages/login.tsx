import React, { useContext, useState } from "react";
import { useAuth } from "../services/useAuth";
import a from "../services/axios";
import * as jose from "jose";

const Login = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [correctCred, setCorrectCred] = useState(true);
  const [missing, setMissing] = useState(false);

  const handleLogin = async (email: string, password: string) => {
    if (!email || !password) {
      setMissing(true);
      setCorrectCred(true);
      return;
    }

    setMissing(false);
    const token = await new jose.SignJWT({ password })
      .setProtectedHeader({ alg: "HS256" })
      .setExpirationTime("1h")
      //ignore env error below
      .sign(new TextEncoder().encode(import.meta.env.VITE_JWTKEY));

    a.post("/login", {
      email,
      token,
    }).then((res) => {
      setCorrectCred(res.data ? true : false);
      if (res.data) {
        login(res.data);
        window.location.href = "/";
      }
    });
  };

  return (
    <div className="flex min-h-full w-screen flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h2 className="mt-6 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
          Sign in to your account
        </h2>
      </div>

      <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
        <div className="bg-white px-6 py-12 shadow sm:rounded-lg sm:px-12">
          <div className="space-y-6">
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium leading-6 text-gray-900 text-left"
              >
                Email address
              </label>
              <div className="mt-2">
                <input
                  id="email"
                  name="email"
                  type="email"
                  autoComplete="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-tropicalIndigo sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium leading-6 text-gray-900 text-left"
              >
                Password
              </label>
              <div className="mt-2">
                <input
                  id="password"
                  name="password"
                  type="password"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-tropicalIndigo sm:text-sm sm:leading-6"
                />
              </div>
            </div>
            <div>
              {missing && (
                <div
                  className="flex items-center bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative"
                  role="alert"
                >
                  <div className="flex items-center">
                    <span className="block sm:inline">
                      Please fill out all fields.
                    </span>
                  </div>
                </div>
              )}
              {!correctCred && (
                <div
                  className="flex items-center bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative"
                  role="alert"
                >
                  <div className="flex items-center">
                    <span className="block sm:inline">
                      Incorrect Email or Password.
                    </span>
                  </div>
                </div>
              )}
            </div>

            <div>
              <button
                onClick={() => handleLogin(email, password)}
                className="flex w-full justify-center rounded-md bg-tropicalIndigo px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-tropicalIndigo-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-tropicalIndigo"
              >
                Sign in
              </button>
            </div>
          </div>
        </div>

        <p className="mt-10 text-center text-sm text-gray-500">
          Not a member?{" "}
          <a
            href="/register"
            className="font-semibold leading-6 text-tropicalIndigo hover:text-tropicalIndigo-600"
          >
            Register Here
          </a>
        </p>
      </div>
    </div>
  );
};

export default Login;
