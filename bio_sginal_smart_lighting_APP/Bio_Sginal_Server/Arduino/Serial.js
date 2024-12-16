//testsensordata.ino
const { SerialPort, ReadlineParser } = require("serialport");

let arduinoPort;
let parser;

const initializeSerialPort = () => {
  return SerialPort.list()
    .then((ports) => {
      if (ports.length === 0) {
        console.log("사용 가능한 시리얼 포트가 없습니다.");
        return Promise.reject("No available ports");
      }

      const portInfo = ports[0];
      console.log(`감지된 포트:${portInfo.path}`);
      arduinoPort = new SerialPort({ path: portInfo.path, baudRate: 9600 });
      parser = arduinoPort.pipe(new ReadlineParser({ delimiter: "\n" }));

      arduinoPort.on("open", () => {
        console.log(`시리얼 포트(${portInfo.path})에 연결되었습니다.`);
      });

      arduinoPort.on("close", () => {
        console.log("시리얼 포트 연결이 해제되었습니다.");
      });

      arduinoPort.on("error", (err) => {
        console.error("시리얼 포트 오류: ", err.message);
      });

      return parser; // parser 반환
    })
    .catch((err) => {
      console.error("포트 감지 오류: ", err);
      return Promise.reject(err);
    });
};

// 아두이노에 데이터 전송하는 함수 추가
const sendDataToArduino = (data) => {
  if (arduinoPort && arduinoPort.isOpen) {
    arduinoPort.write(data + "\n", (err) => {
      if (err) {
        console.error("Error writing to serial port:", err);
      } else {
        console.log("Data sent to Arduino:", data);
      }
    });
  } else {
    console.error("Arduino port is not open. Cannot send data.");
  }
};

// 시리얼 포트 연결 상태 감시
setInterval(() => {
  if (!arduinoPort || !arduinoPort.isOpen) {
    console.log("시리얼 포트가 열려 있지 않습니다. 다시 시도합니다...");
    initializeSerialPort(); // 연결 재시도
  }
}, 5000);

// 시리얼 포트 연결 상태 감시 및 재연결
// setInterval(() => {
//   if (!arduinoPort || !arduinoPort.isOpen) {
//     console.log("시리얼 포트가 열려 있지 않습니다. 다시 시도합니다...");
//     initializeSerialPort()
//       .then((newParser) => {
//         parser = newParser;
//         console.log("포트 재연결 성공");
//       })
//       .catch((err) => {
//         console.error("포트 재연결 실패: ", err);
//       });
//   }
// }, 5000); // 5초마다 재연결 시도

module.exports = {
  initializeSerialPort,
  sendDataToArduino,
};
