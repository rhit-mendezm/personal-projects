import React, { useEffect } from "react";
import Navbar from "../components/navbar";
import a from "../services/axios";
import { Org } from "../components/createPost";

const MyOrgsPage = () => {
    const userID = localStorage.getItem("user");
    const [orgs, setOrgs] = React.useState([] as Org[]);

    if (!userID) {
        window.location.href = "/login";
    }

    const fetchOrgs = () => {
        a.post("/getParticipatingOrgs", {
            userID: userID
        }).then((res) => { 
            setOrgs(res.data.recordset);
        })
    }

    const leaveOrg = (orgID: number) => {
        if (confirm("Are you sure you want to leave this organization?")) {
            a.post("/leaveOrg", {
                userID: userID,
                orgID: orgID
            }).then(() => {
                alert("You have successfully left this organization!");
                fetchOrgs();
            })
        }
    }

    useEffect(() => {
        fetchOrgs();
    }, []);
    
    return (
        <>
            <Navbar />
            <div className="mt-20">
                <h1 className="text-2xl">My Organizations</h1>
                <button className="rounded-md border-2 m-4 border-tropicalIndigo py-1.5 px-4" onClick={() => window.location.href = "/organizations"}>Join Organizations</button>

                <div className="flex flex-wrap px-20">
                    {orgs.map((org) => {
                        return (
                            <div key={org.ID} className="border p-6 m-2 rounded-md">
                                <h2 className="text-xl mb-2">{org.Name}</h2>
                                <button className="rounded-md bg-red-500 py-1.5 px-4 text-white" onClick={() => leaveOrg(org.ID)}>Leave</button>
                            </div>
                        )
                    })}
                </div>
            </div>
        </>
    )
}

export default MyOrgsPage;