import React, { Fragment, useEffect, useState } from "react";
import a from "../services/axios";
import {
  CheckIcon,
  ChevronUpDownIcon,
} from "@heroicons/react/24/solid";
import Navbar from "../components/navbar";
import { Listbox, Transition } from "@headlessui/react";
import CreatePost, { Org } from "../components/createPost";
import SinglePost from "../components/singlePost";

export interface Tag {
  ID: number;
  Name: string;
}

const Home = () => {
  const userID = localStorage.getItem("user");
  const [isAdmin, setAdmin] = useState(false);
  const [posts, setPosts] = useState([]);
  const [sortBy, setSortBy] = useState("latest");
  const [tags, setTags] = useState([] as Tag[]);
  const [selectedTag, setSelectedTag] = useState({
    ID: 0,
    Name: "None",
  } as Tag);
  const [organization, setOrganization] = useState({ ID: 0, Name: "None" } as Org);
  const [participatingOrgs, setParticipatingOrgs] = useState([] as Org[]);

  const getTags = () => {
    a.get("/fetchAllTags").then((res) => {
      setTags([{ ID: 0, Name: "None" }, ...res.data?.recordset]);
    });
  };

  const getParticipatingOrgs = () => {
    a.post("/getParticipatingOrgs", {
      userID
    }).then((res) => {
      setParticipatingOrgs([{ ID: 0, Name: "None" }, ...res.data?.recordset]);
    });
  }

  const getAdminStatus = () => {
    a.post("/checkIfAdmin", {
      userID
    }).then((res) => {
      setAdmin(res.data.recordset[0].isAdmin ? true : false);
    })
  }

  const getSchoolThenPosts = () => {
    a.post("/fetchSchool", {
      userID,
    }).then((res) => {
      if (sortBy == "latest") {
        a.post("/feed", {
          UserID: userID,
          SchoolID: res.data,
          OrganizationID : organization.ID || null,
          Tag: selectedTag.ID || null,
        }).then((res) => {
          setPosts(res.data?.recordset);
        });
      }
      else {
        a.post("/sortlikes", {
          UserID: userID,
          SchoolID: res.data,
          OrganizationID : organization.ID || null,
          Tag: selectedTag.ID || null,
        }).then((res) => {
          setPosts(res.data?.recordset);
        })
      }
    });
  };

  useEffect(() => {
    getTags();
    getAdminStatus();
    getParticipatingOrgs();
  }, []);

  useEffect(() => {
    getSchoolThenPosts();
  }, [selectedTag, organization, sortBy, isAdmin]);

  function classNames(...classes) {
    return classes.filter(Boolean).join(" ");
  }

  return (
    <>
      <Navbar />
      <div className="mt-20">
        <div className="flex flex-col px-20 md:px-48">
          {!userID ? (
            <div className="text-center mx-auto max-w-5xl p-4 border-gray-600 rounded-lg shadow-md">
              <h1>Discover your community!</h1>
              <a href="/login">
                <button className="mt-4 bg-tropicalIndigo hover:bg-tropicalIndigo-dark text-white font-bold py-2 px-4 rounded">
                  Login
                </button>
              </a>
            </div>
          ) : (
            <>
              <CreatePost refreshPosts={getSchoolThenPosts} />
              {/* filters */}
              <div className="max-w-48 text-left">
                <Listbox value={selectedTag} onChange={setSelectedTag}>
                  {({ open }) => (
                    <>
                      <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">
                        Filter By Tag
                      </Listbox.Label>
                      <div className="relative mt-2">
                        <Listbox.Button className="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-tropicalIndigo sm:text-sm sm:leading-6">
                          <span className="block truncate">
                            {selectedTag.Name}
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
                            {tags.map((tag) => (
                              <Listbox.Option
                                key={tag.ID}
                                className={({ active }) =>
                                  classNames(
                                    active
                                      ? "bg-tropicalIndigo text-white"
                                      : "text-gray-900",
                                    "relative cursor-default select-none py-2 pl-3 pr-9",
                                  )
                                }
                                value={tag}
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
                                      {tag.Name}
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

                {/* select organization */}
                <Listbox value={organization} onChange={setOrganization}>
                  {({ open }) => (
                    <>
                      <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900 mt-2">
                        Filter By Organization
                      </Listbox.Label>
                      <div className="relative mt-2">
                        <Listbox.Button className="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-tropicalIndigo sm:text-sm sm:leading-6">
                          <span className="block truncate">
                            {organization.Name}
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
                            {participatingOrgs.map((org) => (
                              <Listbox.Option
                                key={org.ID}
                                className={({ active }) =>
                                  classNames(
                                    active
                                      ? "bg-tropicalIndigo text-white"
                                      : "text-gray-900",
                                    "relative cursor-default select-none py-2 pl-3 pr-9",
                                  )
                                }
                                value={org}
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
                                      {org.Name}
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

                <div className="max-w-48 text-left mt-2">
                  <Listbox value={sortBy} onChange={setSortBy}>
                    {({ open }) => (
                      <>
                        <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">
                          Sort By
                        </Listbox.Label>
                        <div className="relative mt-2">
                          <Listbox.Button className="relative w-full cursor-default rounded-md bg-white py-1.5 pl-3 pr-10 text-left text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:outline-none focus:ring-2 focus:ring-tropicalIndigo sm:text-sm sm:leading-6">
                            <span className="block truncate">
                              {sortBy}
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
                              {[{ Name: "latest" }, { Name: "popularity" }].map((option, i) => (
                                <Listbox.Option
                                  key={i}
                                  className={({ active }) =>
                                    classNames(
                                      active
                                        ? "bg-tropicalIndigo text-white"
                                        : "text-gray-900",
                                      "relative cursor-default select-none py-2 pl-3 pr-9",
                                    )
                                  }
                                  value={option.Name}
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
                                        {option.Name}
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
                </div>
              </div>
              <div>
                {posts &&
                  posts.map((post: any) => <SinglePost key={post.ID} post={post} userID={userID} isAdmin={isAdmin} refreshPosts={getSchoolThenPosts} />)}
              </div>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default Home;
