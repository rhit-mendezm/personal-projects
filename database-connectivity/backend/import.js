require("dotenv").config();
const Papa = require("papaparse");
const sql = require("mssql");
const fs = require("fs");

const sqlConfig = {
  user: process.env.DB_USER,
  password: process.env.DB_PASS,
  database: process.env.DB_DATABASE,
  server: process.env.DB_SERVER,
  options: {
    encrypt: false,
  },
};

const importUsers = async () => {
  //import Users
  const file = fs.createReadStream("SampleData/unnormalized.csv");
  const users = [];

  Papa.parse(file, {
    delimiter: ",",
    newline: "",
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,

    step: (result) => {
      // Called for each row in the file
      try {
        //connect to sql server
        if (!users.includes(result.data.UserEmail)) {
        sql.connect(sqlConfig).then(() => {
          const request = new sql.Request();
          request.input("email", sql.VarChar, result.data.UserEmail);
          request.input("phone", sql.VarChar, result.data.UserPhone);
          request.input("hash", sql.VarChar, result.data.UserHash);
          request
            .execute("CreateUser")
            .then(() => {
              console.log("user created");
            })
            .catch((err) => {
              console.log(err);
            });
        });
      }
      users.push(result.data.UserEmail);
      } catch (err) {
        console.log(err);
      }
    },

    complete: () => {
      console.log("All users successfully processed.");
    },
    error: (error) => {
      console.error("Parsing error:", error.message);
    },
  });
};

const importTags = async () => {
  //import Tags
  const file = fs.createReadStream("SampleData/unnormalized.csv");
  const tags = [];
  Papa.parse(file, {
    delimiter: ",",
    newline: "",
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,

    step: (result) => {
      // Called for each row in the file
      //SchoolName,OrganizationName,SchoolAddress,UserEmail,UserPhone,PostContent,Timestamp,LikeCount,Tag,Reposted?,OriginalPostContent,RepostLikeCount,RepostedTimeStamp
      try {
        //connect to sql server
        if (!tags.includes(result.data.Tag) && result.data.Tag !== "null") {
        sql.connect(sqlConfig).then(() => {
          const request = new sql.Request();
          request.input("Name", sql.VarChar, result.data.Tag);
          request
            .execute("CreateTag")
            .then(() => {
              console.log("tag created");
            })
            .catch((err) => {
              console.log(err);
            });
        });
      }
      tags.push(result.data.Tag);
      } catch (err) {
        console.log(err);
      }
    },

    complete: () => {
      console.log("All tags successfully processed.");
    },
    error: (error) => {
      console.error("Parsing error:", error.message);
    },
  });
};

const importPosts = async () => {
  //import Posts
  const file = fs.createReadStream("SampleData/unnormalized.csv");
  Papa.parse(file, {
    delimiter: ",",
    newline: "",
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,

    step: (result) => {
      // Called for each row in the file
      try {
        //connect to sql server
        let TagID = null;
        sql
          .connect(sqlConfig)
          .then(() => {
            const TagIDRequest = new sql.Request();
            TagIDRequest.input("Name", sql.VarChar, result.data.Tag);
            TagIDRequest.execute("FindTagIDByName")
              .then((res) => {
                TagID = res.recordset[0]?.ID;
              })
              .then(() => {
                const request = new sql.Request();
                request.input("PosterEmail", sql.VarChar, result.data.UserEmail);
                request.input("PostContent", sql.VarChar, result.data.PostContent);
                request.input("TagID", sql.Int, TagID);
                request.input("SchoolName", sql.VarChar, result.data.SchoolName);
                request.input("OrganizationName", sql.VarChar, result.data.OrganizationName || null);
                request.execute("ImportPostHelper").then(() => {
                  console.log("post created");
                });
              });
          })
          .catch((err) => {
            console.log(err);
          });
      } catch (err) {
        console.log(err);
      }
    },

    complete: () => {
      console.log("All posts successfully processed.");
    },
    error: (error) => {
      console.error("Parsing error:", error.message);
    },
  });
};

const importSchools = async () => {
  //import Schools
  const file = fs.createReadStream("SampleData/unnormalized.csv");
  const schools = [];

  Papa.parse(file, {
    delimiter: ",",
    quoteChar: '"',
    newline: "",
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,

    step: (result) => {
      // Called for each row in the file
      try {
        if (!schools.includes(result.data.SchoolName)) {
        //connect to sql server
          sql.connect(sqlConfig).then(() => {
            const request = new sql.Request();
            request.input("name", sql.VarChar, result.data.SchoolName);
            request.input("address", sql.VarChar, result.data.SchoolAddress);
            request
              .execute("CreateSchool")
              .then(() => {
                console.log("school created");
              })
              .catch((err) => {
                console.log(err);
              });
          });
        }
        schools.push(result.data.SchoolName);
      } catch (err) {
        console.log(err);
      }
    },

    complete: () => {
      console.log("All schools successfully processed.");
    },
    error: (error) => {
      console.error("Parsing error:", error.message);
    },
  });
};

const importOrganizations = async () => {
  //import Schools
  const file = fs.createReadStream("SampleData/unnormalized.csv");
  const organizations = [];
  Papa.parse(file, {
    delimiter: ",",
    newline: "",
    header: true,
    dynamicTyping: true,
    skipEmptyLines: true,

    step: (result) => {
      // Called for each row in the file
      try {
        //connect to sql server
        if (!organizations.includes(result.data.OrganizationName) && result.data.OrganizationName != "null") {
          let SchoolID = null;
          sql.connect(sqlConfig).then(() => {
            const SchoolIDRequest = new sql.Request();
            SchoolIDRequest.input("Name", sql.VarChar, result.data.SchoolName);
            SchoolIDRequest.execute("GetSchoolIDByName")
              .then((res) => {
                SchoolID = res.recordset[0]?.ID;
              }).then(() => {
                sql.connect(sqlConfig).then(() => {
                  const request = new sql.Request();
                  request.input("Name", sql.VarChar, result.data.OrganizationName);
                  request.input("UserID", sql.Int, null);
                  request.input("AdminEmail", sql.VarChar, result.data.UserEmail);
                  console.log(SchoolID);
                  request.input("SchoolID", sql.Int, SchoolID);
                  request
                    .execute("CreateOrganization")
                    .then(() => {
                      console.log("organization created");
                    })
                    .catch((err) => {
                      console.log(err);
                    });
                });
              });
          });
      }
      organizations.push(result.data.OrganizationName);
      } catch (err) {
        console.log(err);
      }
    },

    complete: () => {
      console.log("All organizations successfully processed.");
    },
    error: (error) => {
      console.error("Parsing error:", error.message);
    },
  });
};

const importAll = async () => {
  await importUsers();
  await importTags();
  await importSchools();
  await new Promise(r => setTimeout(r, 1000));
  await importOrganizations();
  await new Promise(r => setTimeout(r, 1000));
  await importPosts();
};

importAll();
