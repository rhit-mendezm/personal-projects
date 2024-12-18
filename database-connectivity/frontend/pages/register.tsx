import React, { Fragment, useEffect, useState } from "react";
import { useAuth } from "../services/useAuth";
import a from "../services/axios";
import * as jose from "jose";
import { Listbox, Transition } from "@headlessui/react";
import { ChevronUpDownIcon, CheckIcon } from "@heroicons/react/24/solid";

const Register = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [missing, setMissing] = useState(false);
  const [schools, setSchools] = useState([] as any[]);
  const [school, setSchool] = useState({ ID: 0, Name: "Pick A School" } as any);

  const getSchools = async () => {
    a.get("/fetchSchools").then((res) => {
      setSchools([...res.data?.recordset]);
   });
  }

  const handleRegister = async (email: string, password: string) => {
    if (!email || !password || school.ID == 0) {
      setMissing(true);
      return;
    }

    setMissing(false);
    const token = await new jose.SignJWT({ password })
      .setProtectedHeader({ alg: "HS256" })
      .setExpirationTime("1h")
      //ignore env error below
      .sign(new TextEncoder().encode(import.meta.env.VITE_JWTKEY));

    a.post("/register", {
      email,
      token,
      schoolID: school.ID,
    }).then((res) => {
      if (res.data == "errEmail") {
        alert("Email already in use.");
      }
      if (res.data.returnValue == 0) {
        window.location.href = "/login";
      }
    });
  };

  function classNames(...classes) {
    return classes.filter(Boolean).join(" ");
  }

  useEffect(() => {
    getSchools();
  }, []);

  return (
    <>
      <div className="flex min-h-full w-screen flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-md">
          <h2 className="mt-6 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Register for an account
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
                    autoComplete="current-password"
                    required
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-tropicalIndigo sm:text-sm sm:leading-6"
                  />
                </div>
              </div>
              <Listbox value={school} onChange={setSchool}>
                {({ open }) => (
                  <>
                    <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">
                      Select your school
                    </Listbox.Label>
                    <div className="relative mt-2">
                      <Listbox.Button className="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-tropicalIndigo sm:text-sm sm:leading-6">
                        <span className="block truncate">
                          {school.Name}
                        </span>
                        <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                          <ChevronUpDownIcon
                            className="h-5 w-5 text-gray-400"
                            aria-hidden="true"
                          />
                        </span>
                      </Listbox.Button>

                      <Transition
                        show={open}
                        as={Fragment}
                        leave="transition ease-in duration-100"
                        leaveFrom="opacity-100"
                        leaveTo="opacity-0"
                      >
                        <Listbox.Options className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                          {schools.map((sch) => (
                            <Listbox.Option
                              key={sch.ID}
                              className={({ active }) =>
                                classNames(
                                  active
                                    ? "bg-tropicalIndigo text-white"
                                    : "text-gray-900",
                                  "relative cursor-default select-none py-2 pl-3 pr-9",
                                )
                              }
                              value={sch}
                            >
                              {({ selected, active }) => (
                                <>
                                  <span
                                    className={classNames(
                                      selected
                                        ? "font-semibold"
                                        : "font-normal",
                                      "block truncate",
                                    )}
                                  >
                                    {sch.Name}
                                  </span>
                                  {selected ? (
                                    <span
                                      className={classNames(
                                        active
                                          ? "text-white"
                                          : "text-tropicalIndigo",
                                        "absolute inset-y-0 right-0 flex items-center pr-4",
                                      )}
                                    >
                                      <CheckIcon
                                        className="h-5 w-5"
                                        aria-hidden="true"
                                      />
                                    </span>
                                  ) : null}
                                </>
                              )}
                            </Listbox.Option>
                          ))}
                        </Listbox.Options>
                      </Transition>
                    </div>
                  </>
                )}
              </Listbox>
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
              <div>
                <button
                  onClick={() => handleRegister(email, password)}
                  className="flex w-full justify-center rounded-md bg-tropicalIndigo px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-tropicalIndigo-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-tropicalIndigo"
                >
                  Register
                </button>
              </div>
            </div>
          </div>

          <p className="mt-10 text-center text-sm text-gray-500">
            Already a member?{" "}
            <a
              href="/login"
              className="font-semibold leading-6 text-tropicalIndigo hover:text-tropicalIndigo-600"
            >
              Login Here
            </a>
          </p>
        </div>
      </div>
    </>
  );
};

export default Register;
