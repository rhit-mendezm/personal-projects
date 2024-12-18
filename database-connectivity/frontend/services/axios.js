import axios from "axios";

const a = axios.create({
  baseURL: "http://localhost:3000",
});

export default a;
