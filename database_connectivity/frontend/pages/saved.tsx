import React, { useEffect, useState } from "react";
import a from "../services/axios";
import Navbar from "../components/navbar";
import SinglePost from "../components/singlePost";

const Saved = () => {
  const userID = localStorage.getItem("user");
  const [isAdmin, setAdmin] = useState(false);
  const [posts, setPosts] = useState([]);

  const getSavedPosts = () => {
    a.post("/fetchSaved", {
      userID
    }).then((res) => {
      setPosts(res.data?.recordset);
    })
  };

  const getAdminStatus = () => {
    a.post("/checkIfAdmin", {
      userID
    }).then((res) => {
      setAdmin(res.data ? true : false);
    })
  }

  useEffect(() => {
    getAdminStatus();
    getSavedPosts();
  }, []);

  return (
    <>
      <Navbar />
      <div className="mt-20">
        <div className="flex flex-col px-20 md:px-48">
          {!userID ? (
            <div className="text-center mx-auto max-w-5xl p-4 border-gray-600 rounded-lg shadow-md">
              <h1>Login to see your saved posts!</h1>
              <a href="/login">
                <button className="mt-4 bg-tropicalIndigo hover:bg-tropicalIndigo-dark text-white font-bold py-2 px-4 rounded">
                  Login
                </button>
              </a>
            </div>
          ) : (
            <>
              <div>
                {posts &&
                  posts.map((post: any) => <SinglePost key={post.ID} post={post} userID={userID} isAdmin={isAdmin} refreshPosts={getSavedPosts} />)}
              </div>
              <div>
                {posts.length < 1 &&
                  (
                    <div className="text-center mx-auto max-w-5xl p-4 border-gray-600 rounded-lg shadow-md">
                      <h1>Posts that you have saved will appear here!</h1>
                      <a href="/">
                        <button className="mt-4 bg-tropicalIndigo hover:bg-tropicalIndigo-dark text-white font-bold py-2 px-4 rounded">
                          Home
                        </button>
                      </a>
                    </div>
                  )
                }
              </div>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default Saved;