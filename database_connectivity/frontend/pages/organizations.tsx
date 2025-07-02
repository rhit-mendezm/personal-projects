import React, { useEffect } from "react";
import Navbar from "../components/navbar";
import a from "../services/axios";
import { Org } from "../components/createPost";

const OrganizationsPage = () => {
    const userID = localStorage.getItem("user");
    const [orgs, setOrgs] = React.useState([] as Org[]);
    const [isAdmin, setIsAdmin] = React.useState(false);
    const [newOrgName, setNewOrgName] = React.useState("");
    const [email, setEmail] = React.useState("");

    if (!userID) {
        window.location.href = "/login";
    }

    const fetchOrgs = () => {
        a.post("/fetchOrgs", {
            userID: userID
        }).then((res) => {
            setOrgs(res.data.recordset);
        })
    }

    const createOrg = () => {
        if (!newOrgName || !newOrgName.trim()) {
            alert("Please enter a name for your organization.");
            return;
        }   

        if (!email || !email.trim()) {
            alert("Please enter an email for your organization's admin.");
            return;
        }

        a.post("/createOrg", {
            userID: userID,
            orgName: newOrgName,
            adminEmail: email
        }).then((res) => {
            if (res.data) {
                alert("You have successfully created a new organization!");
                setNewOrgName("");
                setEmail("");
                fetchOrgs();
            } else {
                alert("A user with that email cannot be found.");
            }
        })
    }

    const getIfAdmin = () => {
        a.post("/checkIfAdmin", {
            userID: userID
        }).then((res) => {
            setIsAdmin(res.data.recordset[0].isAdmin);
        })
    }

    const joinOrg = (orgID: number) => {
        if (confirm("Are you sure you want to join this organization?")) {
            a.post("/joinOrg", {
                userID: userID,
                orgID: orgID
            }).then(() => {
                alert("You have successfully joined this organization!");
                fetchOrgs();
            })
        }
    }

    useEffect(() => {
        getIfAdmin();
        fetchOrgs();
    }, []);
    
    return (
        <>
            <Navbar />
            <div className="mt-20">
                <h1 className="text-2xl">Discover Organizations</h1>
                <button className="rounded-md border-2 m-4 border-tropicalIndigo py-1.5 px-4" onClick={() => window.location.href = "/myOrgs"}>See your Organizations</button>

                <div className="flex flex-wrap px-20">
                    {isAdmin && (
                        <div className="border-2 p-6 m-2 rounded-md border-apricot-500">
                            <h2 className="text-lg mb-2">Create a new organization</h2>
                            <input className="border rounded-md p-1 mr-2" value={newOrgName} onChange={(e) => setNewOrgName(e.target.value)} placeholder="Org Name"/>
                            <input className="border rounded-md p-1 mr-2" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Org Admin Email"/>
                            <button className="rounded-md bg-tropicalIndigo py-1.5 px-4 text-white" onClick={createOrg}>Create</button>
                        </div>
                    )}

                    {orgs.map((org) => {
                        return (
                            <div key={org.ID} className="border p-6 m-2 rounded-md">
                                <h2 className="text-xl mb-2">{org.Name}</h2>
                                <button className="rounded-md bg-tropicalIndigo py-1.5 px-4 text-white" onClick={() => joinOrg(org.ID)}>Join</button>
                            </div>
                        )
                    })}
                </div>
            </div>
        </>
    )
}

export default OrganizationsPage;