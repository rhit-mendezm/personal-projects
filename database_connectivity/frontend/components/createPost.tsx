import React, { Fragment, useEffect, useState } from "react";
import a from "../services/axios";
import { Listbox, Transition } from "@headlessui/react";
import { ChevronUpDownIcon } from "@heroicons/react/24/solid";
import { CheckIcon } from "@heroicons/react/24/solid";
import { Tag } from "../pages/home";


export interface Org {
    ID: number;
    Name: string;
}
 
const CreatePost = ({refreshPosts}) => {
    const userID = localStorage.getItem("user");
 
    const [content, setContent] = useState("");
    const [organization, setOrganization] = useState({ID: 0, Name: "None"} as Org);
    const [participatingOrgs, setParticipatingOrgs] = useState([] as Org[]);
    const [tags, setTags] = useState([] as Tag[]);
    const [selectedTag, setSelectedTag] = useState({
        ID: 0,
        Name: "None",
      } as Tag);

    const getParticipatingOrgs = () => {
        a.post("/getParticipatingOrgs", {
            userID
        }).then((res) => {
            setParticipatingOrgs([{ ID: 0, Name: "None" }, ...res.data?.recordset]);
        });
    }

    const getTags = () => {
        a.get("/fetchAllTags").then((res) => {
            setTags([{ ID: 0, Name: "None" }, ...res.data?.recordset]);
        });
    };
 
    const handleCreatePost = () => {
        if (!content || !content.trim()) {
            return;
        }

        if (organization.ID != 0) {
            a.post("/postInOrg", {
                userID,
                organizationID: organization.ID,
                content,
                tagID: selectedTag.ID || null
            }).then((res) => {
                setContent("");
                refreshPosts();
            })
        } else {
            a.post("/postInSchool", {
                userID,
                content,
                tagID: selectedTag.ID || null
            }).then((res) => {
                setContent("");
                refreshPosts();
            })
        }
    }

    function classNames(...classes) {
        return classes.filter(Boolean).join(" ");
    }

    useEffect(() => {
        getTags();
        getParticipatingOrgs();
    }, []);
   
    return (
        <>
            <div className="flex flex-col items-center">
                <div className="mt-4 p-4 border-gray-600 rounded-lg shadow-md min-w-[500px] text-left">
                    <input
                        type="text"
                        placeholder="What's on your mind?"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded-md mb-4"
                    />
                    <Listbox value={organization} onChange={setOrganization}>
                        {({ open }) => (
                            <>
                            <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900">
                                Post in Organization:
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
                    <Listbox value={selectedTag} onChange={setSelectedTag}>
                    {({ open }) => (
                        <>
                        <Listbox.Label className="block text-sm font-medium leading-6 text-gray-900 mt-2 ">
                            Select a Tag:
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
                    <button
                        onClick={() => handleCreatePost()}
                        className="mt-2 bg-tropicalIndigo hover:bg-tropicalIndigo-dark text-white font-bold py-2 px-4 rounded"
                    >
                        Post
                    </button>
                </div>
            </div>
        </>
    );
}
 
export default CreatePost;