import React, { useEffect, useState } from "react";
import { BookmarkIcon as BookmarkIconOutline } from "@heroicons/react/24/outline";
import { BookmarkIcon as BookmarkIconSolid } from "@heroicons/react/24/solid";
import a from "../services/axios";

function SaveButton({ postID }) {
  const userID = localStorage.getItem("user");
  const [isSaved, setSaved] = useState(false);

  const getInitialState = () => {
    a.post("/checkIfSaved", {
      userID,
      postID,
    }).then((res) => {
      setSaved(res.data);
    });
  };

  const handleClick = () => {
    if (!isSaved) {
      a.post("/SavePost", {
        userID,
        postID,
      }).then((res) => {
        setSaved(!isSaved);
      });
    } else {
      a.post("/UnsavePost", {
        userID,
        postID,
      }).then((res) => {
        setSaved(!isSaved);
      });
    }
  };

  useEffect(() => {
    getInitialState();
  }, []);

  if (isSaved) {
    return (
      <>
        <BookmarkIconSolid onClick={handleClick} className="h-6 w-6" />
      </>
    );
  } else {
    return (
      <>
        <BookmarkIconOutline onClick={handleClick} className="h-6 w-6 text-gray-400" />
      </>
    );
  }
}

export default SaveButton;
