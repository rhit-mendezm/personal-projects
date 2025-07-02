import React, { useEffect, useState } from "react";
import { HeartIcon } from "@heroicons/react/24/solid";
import a from "../services/axios";

function LikeButton({ postID, likeCount }) {
  const userID = localStorage.getItem("user");
  const [isLiked, setLiked] = useState(false);
  const [likes, setCount] = useState(likeCount);

  const getInitialState = () => {
    a.post("/checkIfLiked", {
      userID,
      postID,
    }).then((res) => {
      setLiked(res.data);
    });
  };

  const handleClick = () => {
    if (!isLiked) {
      a.post("/LikePost", {
        userID,
        postID,
      }).then((res) => {
        setLiked(!isLiked);
        setCount(likes + 1);
      });
    } else {
      a.post("/RemoveLike", {
        userID,
        postID,
      }).then((res) => {
        setLiked(!isLiked);
        setCount(likes - 1);
      });
    }
  };

  useEffect(() => {
    getInitialState();
  }, []);

  if (isLiked) {
    return (
      <>
        <HeartIcon onClick={handleClick} className="h-6 w-6 text-red-400" />
        {likes}
      </>
    );
  } else {
    return (
      <>
        <HeartIcon onClick={handleClick} className="h-6 w-6 text-gray-300" />
        {likes}
      </>
    );
  }
}

export default LikeButton;
