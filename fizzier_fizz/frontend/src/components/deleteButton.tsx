import React from "react";
import { TrashIcon } from "@heroicons/react/24/outline";
import a from "../services/axios";

function DeleteButton({ postID, refreshPosts }) {
  const handleClick = () => {
    if(confirm("Are you sure you want to delete this post?")) {
      a.post("/deletePost", {
        postID
      }).then((res) => {
        refreshPosts();
      })
    }
  }

  return (
    <TrashIcon onClick={handleClick} className="h-6 w-6 text-red-400" />
  )
}

export default DeleteButton;