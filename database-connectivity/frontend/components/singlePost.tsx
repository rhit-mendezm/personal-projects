import React from "react";
import { formatTimeDifference } from "../services/processTimestamp";
import LikeButton from "../components/likeButton";
import SaveButton from "../components/saveButton";
import BlockUser from "../components/blockUser";
import DeleteButton from "./deleteButton";

function SinglePost({ post, userID, isAdmin, refreshPosts }) {
  return (
    <div
      key={post.ID}
      className="mt-4 p-4 border border-gray-600 rounded-lg shadow-md min-w-[500px] text-left"
    >
      <a href={`/post/${post.ID}`}>
        <div className="flex gap-2 items-center mb-4">
          {post.Tag && (
            <div className="text-center pt-1 pb-2 px-4 rounded-full bg-apricot w-fit">
              {post.Tag}
            </div>
          )}
          <p className="pb-1">
            {formatTimeDifference(post.Timestamp)}
          </p>
        </div>
        <p>{post.Content}</p>
      </a>
      <div className="flex justify-between items-center">
        <div className="flex items-center my-2 gap-2">
          <div className="flex my-2 gap-2 cursor-pointer">
            <LikeButton
              postID={post.ID}
              likeCount={post.LikeCount}
            />
          </div>
          <div className="ml-8 cursor-pointer">
            <SaveButton
              postID={post.ID}
            />
          </div>
        </div>
        <div className="flex items-center my-2 gap-2">
          {(isAdmin || post.Poster == userID) && <DeleteButton postID={post.ID} refreshPosts={refreshPosts} />}
          {post.Poster != userID && <BlockUser blockeeID={post.Poster} />}
        </div>
      </div>
    </div>
  )
}

export default SinglePost;