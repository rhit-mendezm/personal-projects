import { useParams } from "react-router-dom";
import a from "../services/axios";
import { useEffect, useState } from "react";
import React from "react";
import Navbar from "../components/navbar";
import { formatTimeDifference } from "../services/processTimestamp";
import LikeButton from "../components/likeButton";
import SaveButton from "../components/saveButton";

interface Post {
    ID: number,
    Content: string,
    Timestamp: string,
    LikeCount: number,
    Poster: number,
    Tag: string | null,
}

const Post = () => {
    const user = localStorage.getItem("user");
    const { id } = useParams();

    if (!user) {
        window.location.href = "/login";
    }

    if (!id) {
        window.location.href = "/";
    }

    const [post, setPost] = useState({} as Post);
    const [comments, setComments] = useState([] as Post[])
    const [input, setInput] = useState("" as string);

    const getPost = () => {
        a.post("/fetchPost", {
            postID: id
        }).then((res) => {
            if (!res.data) {
                window.location.href = "/";
                return;
            }
            setPost(res.data.recordset[0]);
        });
    }

    const getComments = () => {
        a.post("/fetchComments", {
            postID: id
        }).then((res) => {
            setComments(res.data.recordset);
        });
    }

    const postComment = () => {
        if (!input) {
            alert("Please enter a comment.");
            return;
        }

        a.post("/comment", {
            userID: user,
            postID: id,
            content: input
        }).then(() => {
            getComments();
        });
    }

    useEffect(() => {
        getPost();
        getComments();
    }, []);

    return (
        <>
            <Navbar />
            <div className="mt-20">
                <div className="flex justify-center">
                    {post.ID && (
                        <div className="w-1/2 bg-white p-4 rounded-lg shadow-lg text-left">
                            <h1 className="text-xl font-bold">{post?.Content}</h1>
                            <p className="text-gray-500">{formatTimeDifference(post?.Timestamp)}</p>
                            <div className="flex gap-4">
                                <div className="flex gap-1">
                                    <LikeButton
                                        postID={post.ID}
                                        likeCount={post.LikeCount}
                                    />
                                </div>
                                <div className="">
                                    <SaveButton
                                        postID={post.ID}
                                    />
                                </div>
                            </div>
                        </div>
                    )}
                </div>
                <div className="mt-10 flex flex-col items-center">
                    <p className="text-lg text-left">Comments:</p>
                    <div className="w-1/2 bg-white p-4 rounded-lg shadow-lg text-left mt-2">
                        <textarea
                            className="w-full h-15 p-2 rounded-md"
                            placeholder="Write a comment..."
                            maxLength={200}
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                        />
                        <button className="bg-tropicalIndigo text-white rounded-lg p-2 mt-2" onClick={() => postComment()}>Submit</button>
                    </div>

                    {comments.map((comment: Post) => (
                        <div key={comment.ID} className="w-1/2 bg-white p-4 rounded-lg shadow-lg text-left mt-2">
                            <h1 className="text-lg">{comment?.Content}</h1>
                            <p className="text-gray-500">{formatTimeDifference(comment?.Timestamp)}</p>
                            <div className="flex gap-4">
                                <div className="flex gap-1">
                                    <LikeButton
                                        postID={comment.ID}
                                        likeCount={comment.LikeCount}
                                    />
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default Post;