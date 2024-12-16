const mysql = require("mysql");

// MySQL 데이터베이스 연결 설정
const connection = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "5340",
  database: "android_project",
});

// 데이터베이스 연결
connection.connect((err) => {
  if (err) {
    console.error("MySQL 연결 오류:", err);
  } else {
    console.log("MySQL 데이터베이스에 성공적으로 연결되었습니다.");
  }
});

// 센서 데이터를 데이터베이스에 저장하는 함수
const sensorSave = (name, bpm, gsr, temperature, humidity) => {
  const query =
    "INSERT INTO sensordata (name, bpm, gsr, temperature, humidity) VALUES (?, ?, ?, ?, ?)";
  connection.query(
    query,
    [name, bpm, gsr, temperature, humidity],
    (err, result) => {
      if (err) {
        console.error("데이터베이스 저장 오류: ", err);
      } else {
        console.log("센서 데이터가 데이터베이스에 성공적으로 저장되었습니다.");
      }
    }
  );
};

// 프로필 정보를 데이터베이스에 저장하는 함수
const profileSave = (image, name, date) => {
  const query = "INSERT INTO profile (image, name, date) VALUES ( ?, ?, ?)";
  connection.query(query, [image, name, date], (err, result) => {
    if (err) {
      console.error("데이터베이스 저장 오류: ", err);
    } else {
      console.log("프로필 정보가 데이터베이스에 성공적으로 저장되었습니다.");
    }
  });
};

const getProfiles = (callback) => {
  const query = "SELECT * FROM profile";
  connection.query(query, (err, results) => {
    if (err) {
      console.error("Error executing query:", err);
      return callback(err); // 에러 콜백 호출
    }
    // console.log("Profiles retrieved:", results);
    callback(null, JSON.stringify(results)); // JSON 문자열로 변환하여 반환
  });
};

module.exports = { sensorSave, profileSave, getProfiles };
