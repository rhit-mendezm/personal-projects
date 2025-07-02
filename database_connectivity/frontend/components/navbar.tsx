import React, { useState } from "react";
import { useAuth } from "../services/useAuth";
import a from "../services/axios";

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const user = localStorage.getItem("user");
  const { logout } = useAuth();

  const toggleNavbar = () => {
    setIsOpen(!isOpen);
  };

  return (
    <nav className="bg-custom-primary">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex-shrink-0">
            <a href="/" className="font-bold text-lg">
              Fizzier Fizz
            </a>
          </div>
          <div className="flex items-center">
            <div className="hidden md:block">
              {user ? (
                <div>
                  <a href="/saved">
                    <button className="mr-4 font-medium px-4 py-1.5 border-2 border-tropicalIndigo rounded-md">
                      Saved Posts
                    </button>
                  </a>
                  <a href="/updateAccount">
                    <button className="mr-4 font-medium px-4 py-1.5 border-2 border-tropicalIndigo rounded-md">
                      Update Profile
                    </button>
                  </a>
                  <a href="/organizations">
                    <button className="mr-4 font-medium px-4 py-1.5 border-2 border-tropicalIndigo rounded-md">
                      Organizations
                    </button>
                  </a>
                  <button
                    className="mr-4 font-medium px-4 py-1.5 border-2 border-tropicalIndigo rounded-md"
                    onClick={() => logout()}
                  >
                    Logout
                  </button>
                </div>
              ) : (
                <div className="flex">
                  <a href="/login">
                    <button className="mr-4 font-medium px-4 py-1.5 border-2 border-tropicalIndigo rounded-md">
                      Log In
                    </button>
                  </a>
                  <a href="/register">
                    <button className="mr-4 font-medium px-4 py-1.5 border-2 border-tropicalIndigo rounded-md bg-tropicalIndigo-600 text-white">
                      Sign Up
                    </button>
                  </a>
                </div>
              )}
            </div>
            <div className="-mr-2 flex md:hidden">
              <button
                onClick={toggleNavbar}
                type="button"
                className="inline-flex items-center justify-center p-2 rounded-md text-tropicalIndigo hover:text-tropicalIndigo-600 hover:bg-custom-secondary focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-custom-primary focus:ring-tropicalIndigo-400"
                aria-controls="mobile-menu"
                aria-expanded="false"
              >
                <span className="sr-only">Open main menu</span>
                {!isOpen ? (
                  <svg
                    className="block h-6 w-6"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    aria-hidden="true"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M4 6h16M4 12h16M4 18h16"
                    />
                  </svg>
                ) : (
                  <svg
                    className="block h-6 w-6"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    aria-hidden="true"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M6 18L18 6M6 6l12 12"
                    />
                  </svg>
                )}
              </button>
            </div>
          </div>
        </div>
      </div>
      <div
        className={`${isOpen ? "block" : "hidden"} md:hidden text-left border-b`}
        id="mobile-menu"
      >
        {user ? (
          <>
            <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
              <a
                href="/saved"
                className="block px-3 py-2 rounded-md font-medium"
              >
                Saved Posts
              </a>
            </div>
            <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
              <a
                href="/updateAccount"
                className="block px-3 py-2 rounded-md font-medium"
              >
                Update Profile
              </a>
            </div>
            <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
              <a
                href="/organizations"
                className="block px-3 py-2 rounded-md font-medium"
              >
                Organizations
              </a>
            </div>
          </>
        ) : (
          <></>
        )}
        <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
          {user ? (
            <button
              className="block px-3 py-2 rounded-md font-medium"
              onClick={() => logout()}
            >
              Logout
            </button>
          ) : (
            <a href="/login">
              <button className="block px-3 py-2 rounded-md font-medium">
                Login
              </button>
            </a>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
