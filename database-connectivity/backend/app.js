require("dotenv").config();
var express = require("express");
var app = express();
var cors = require("cors");
app.use(cors());
var bodyParser = require("body-parser");
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
var jose = require("jose");
const { createHash, getHashes } = require("crypto");
const sql = require("mssql");

const sqlConfig = {
  user: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_DATABASE,
  server: process.env.DB_SERVER,
  options: {
    encrypt: false,
  },
};

app.get("/", function (req, res) {
  res.send("Hello World!");
});

app.post("/login", async function (req, res) {
  const secret = new TextEncoder().encode(process.env.JWTKEY);
  const { payload } = await jose.jwtVerify(req.body.token, secret);

  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("email", sql.VarChar, req.body.email);
    request
      .execute("GetHash")
      .then((result) => {
        res.send(
          result.recordset[0]?.hash ==
            createHash("sha256").update(payload.password).digest("hex")
            ? result.recordset[0]?.ID.toString()
            : null,
        );
      })
      .catch((err) => {
        console.log(err);
      });
  });
});


app.post("/register", async function (req, res) {
  const secret = new TextEncoder().encode(process.env.JWTKEY);
  const { payload } = await jose.jwtVerify(req.body.token, secret);

  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("email", sql.VarChar, req.body.email);
    request.input(
      "hash",
      sql.VarChar,
      createHash("sha256").update(payload.password).digest("hex"),
    );
    request.input("phone", sql.VarChar, null);
    request.input("schoolID", sql.Int, req.body.schoolID);
    request
      .execute("Register")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
        res.send("errEmail");
      });
  });
});

app.post("/updateAccount", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("ID", sql.Int, req.body.ID);
    request.input("Phone", sql.VarChar, req.body.Phone);
    request
      .execute("UpdatePhone")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        res.send("");
        console.log(err);
      });
  });
});

app.post("/feed", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.UserID);
    request.input("SchoolID", sql.Int, req.body.SchoolID);
    request.input("OrganizationID", sql.Int, req.body.OrganizationID)
    request.input("TagID", sql.Int, req.body.Tag);
    request
      .execute("FetchAllPostsInASchool")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        res.send("");
        console.log(err);
      });
  });
});

app.post("/fetchEmail", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("ID", sql.Int, req.body.ID);
    request
      .execute("GetEmail")
      .then((result) => {
        res.send(result.recordset[0]?.Email);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/fetchPhone", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.ID);
    request
      .execute("GetPhone")
      .then((result) => {
        res.send(result.recordset[0]?.Phone);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/fetchSchool", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request
      .execute("FetchSchool")
      .then((result) => {
        res.send(result.recordset[0]?.School.toString());
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/checkIfLiked", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request.input("PostID", sql.Int, req.body.postID);
    request.output("IsLiked", sql.Bit);
    request
      .execute("CheckIfLiked")
      .then((result) => {
        res.send(result.output.IsLiked);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/likePost", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request.input("PostID", sql.Int, req.body.postID);
    request
      .execute("LikePost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/removeLike", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request.input("PostID", sql.Int, req.body.postID);
    request
      .execute("RemoveLike")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.get("/fetchAllTags", function (req, res) {
  sql.connect(sqlConfig).then(() => { 
    const request = new sql.Request();
    request
      .execute("FetchAllTags")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/postInOrg", function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request.input("OrganizationID", sql.Int, req.body.organizationID)
    request.input("Content", sql.VarChar, req.body.content)
    request.input("TagID", sql.Int, req.body.tagID)
    request
      .execute("CreatePostInOrganization")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
})

app.post("/postInSchool", function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("Poster", sql.Int, req.body.userID)
    request.input("Content", sql.VarChar, req.body.content)
    request.input("Tag", sql.Int, req.body.tagID)
    request
      .execute("CreatePost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
})

app.post("/getParticipatingOrgs", function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request
      .execute("getParticipatingOrgs")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
    })
  })

app.post("/savePost", function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request.input("PostID", sql.Int, req.body.postID);
    request
      .execute("SavePost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
})

app.post("/fetchPost", function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("PostID", sql.Int, req.body.postID)
    request
      .execute("FetchPost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        res.send("");
        console.log(err);
      })
  })
})

app.post("/unsavePost", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request.input("PostID", sql.Int, req.body.postID);
    request
      .execute("UnsavePost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/fetchSaved", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request
      .execute("FetchSaved")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/checkIfSaved", async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request.input("PostID", sql.Int, req.body.postID);
    request.output("IsSaved", sql.Bit);
    request
      .execute("CheckIfSaved")
      .then((result) => {
        res.send(result.output.IsSaved);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.post("/blockUser", function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("BlockerID", sql.Int, req.body.blockerID)
    request.input("BlockeeID", sql.Int, req.body.blockeeID)
    request
      .execute("BlockUser")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
})

app.post('/fetchComments', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("PostID", sql.Int, req.body.postID)
    request
      .execute("GetCommentsOfPost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
});

app.post('/comment', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request.input("PostID", sql.Int, req.body.postID)
    request.input("Content", sql.VarChar, req.body.content)
    request
      .execute("CreateComment")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
});

app.post('/sortlikes', async function (req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.UserID);
    request.input("SchoolID", sql.Int, req.body.SchoolID);
    request.input("OrganizationID", sql.Int, req.body.OrganizationID)
    request.input("TagID", sql.Int, req.body.Tag);
    request
      .execute("GetPostsByLikes")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        res.send("");
        console.log(err);
      });
  });
});


app.post('/fetchOrgs', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID);
    request
      .execute("FetchOrgsOfSchool")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
});

app.post('/joinOrg', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request.input("OrgID", sql.Int, req.body.orgID)
    request
      .execute("JoinOrg")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
});

app.post('/leaveOrg', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request.input("OrgID", sql.Int, req.body.orgID)
    request
      .execute("LeaveOrg")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
});

app.post('/checkIfAdmin', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request
      .execute("CheckIfAdmin")
      .then((result) => {
        res.send(result);
      })  
      .catch((err) => {
        console.log(err);
      })
  })
});

app.post('/createOrg', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("UserID", sql.Int, req.body.userID)
    request.input("Name", sql.VarChar, req.body.orgName)
    request.input("AdminEmail", sql.VarChar, req.body.adminEmail)
    request.input("SchoolID", sql.Int, null)
    request
      .execute("CreateOrganization")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
        res.send("");
      })
  })
});

app.post("/deletePost", async function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request.input("PostID", sql.Int, req.body.postID);
    request
      .execute("DeletePost")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
})

app.post("/fetchUsers", async function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request
      .execute("FetchUsers")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      });
  });
});

app.get('/fetchSchools', function(req, res) {
  sql.connect(sqlConfig).then(() => {
    const request = new sql.Request();
    request
      .execute("FetchAllSchools")
      .then((result) => {
        res.send(result);
      })
      .catch((err) => {
        console.log(err);
      })
  })
});

app.listen(3000, function () {
  console.log("Example app listening on port 3000!");
});
