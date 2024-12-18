import React, { useEffect, useState } from "react";
import a from "../services/axios";

const UpdateAccount = () => {
  const ID = localStorage.getItem("user");
  const [Phone, setPhone] = useState("");
  const [clicked, setClicked] = useState(false);
  const [valid, setValid] = useState(false);
  const [Email, setEmail] = useState("");

  const getEmail = () => {
    a.post("/fetchEmail", {
      ID
    }).then((res) => {
      setEmail(res.data);
    });
  };

  const getPhone = () => {
    a.post("/fetchPhone", {
      ID
    }).then((res) => {
      setPhone(res.data);
    })
  }

  const handleUpdateAccount = async () => {
    a.post("/updateAccount", {
      ID,
      Phone,
    }).then((res) => {
      setValid(res.data ? true : false);
      setClicked(true);
    });
  };

  useEffect(() => {
    getEmail();
    getPhone();
  }, []);

  return (
    <>
      <div className="flex min-h-full w-screen flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-md">
          <h2 className="mt-6 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Update account data
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
          <div className="bg-white px-6 py-12 shadow sm:rounded-lg sm:px-12">
            <div className="space-y-6">
              <div>
                <div>
                  <label
                    htmlFor="email"
                    className="block text-sm font-medium leading-6 text-gray-900 text-left"
                  >
                    Email Address
                  </label>
                  <div className="mt-2 text-left">{Email}</div>
                </div>

                <div>
                  <label
                    htmlFor="phone"
                    className="mt-4 block text-sm font-medium leading-6 text-gray-900 text-left"
                  >
                    Phone Number
                  </label>
                  <div className="mt-2">
                    <input
                      type="tel"
                      autoComplete="tel-national"
                      defaultValue={Phone}
                      onChange={(e) => {
                        setPhone(e.target.value)
                      }}
                      className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-tropicalIndigo sm:text-sm sm:leading-6"
                    />
                  </div>
                </div>

                <div>
                  {clicked && !valid && (
                    <p className="mt-3 text-red-500 text-sm">
                      Invalid phone number!
                    </p>
                  )}
                  {clicked && valid && (
                    <p className="mt-3 text-green-500 text-sm">
                      Successfully updated phone number!
                    </p>
                  )}
                </div>

                <div className="mt-7 grid grid-cols-2 gap-8">
                  <a href="/">
                    <button className="flex w-full justify-center rounded-md bg-gray-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-gray-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-gray-600">
                      Cancel
                    </button>
                  </a>

                  <button
                    onClick={() => {
                      handleUpdateAccount()
                    }}
                    className="flex w-full justify-center rounded-md bg-tropicalIndigo px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-tropicalIndigo-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-tropicalIndigo"
                  >
                    Update
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default UpdateAccount;
