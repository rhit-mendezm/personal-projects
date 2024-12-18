import React, { useEffect, useState } from "react";
import { ShieldExclamationIcon } from "@heroicons/react/24/outline";
import a from "../services/axios";

function BlockUser({blockeeID}) {
    const blockerID = localStorage.getItem("user");
    
    const handleClick = () => {
        if (confirm("Are you sure you want to block this user?")) {
            a.post("/blockUser", {
                blockerID,
                blockeeID,
            }).then((res) => {
                alert("Successfully blocked user.")
            })
        }
    }

    return (
        <>
            <ShieldExclamationIcon onClick={() => handleClick()} className="h-6 w-6 cursor-pointer" />
        </>
    )

}

export default BlockUser;