const url = "ws://localhost:8080/quiz-room";
const topic = "/topic/room/f07b1b1f91cc40da84cd69b5e926d5b0/questions";
const app = "/app/room/f07b1b1f91cc40da84cd69b5e926d5b0/next-question";

const token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJHdVZEZGMyaUtkNXptSDZBTXpUU3k1dVdBLXI5a2czel9VVXg5X1FYcTNJIn0.eyJleHAiOjE3MzA3Mzc4MjcsImlhdCI6MTczMDczNzUyNywiYXV0aF90aW1lIjoxNzMwNzM1ODg3LCJqdGkiOiIxYjVlNTc2NC1mNWNjLTQ5MDgtYWJiYy04M2ZiMGJjOThlNGMiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvcmVhbG1zL3F1aXpkZXYiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTIyZWE4MjUtN2Y2YS00YWM5LTlkYmEtZGMyMjIwZGE2Mjg0IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicXVpemNsaWVudCIsInNpZCI6ImZlNGQ4Mzc3LTkyNDItNDk1ZS1hOTllLTBhMDQ3NjU1NzEwMyIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1xdWl6ZGV2Iiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsIlVTRVIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoidGVzdCBkZW1vIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdCIsImdpdmVuX25hbWUiOiJ0ZXN0IiwiZmFtaWx5X25hbWUiOiJkZW1vIiwiZW1haWwiOiJ0ZXN0QGdtYWlsLmNvbSJ9.rFIrciiK1WEtqos4Se4M-FtW0NgOwlc4R3v_vIVXWARKB1q9-Oi8WEadk25tcgZsBs7H7osYeXHXCWSbZk-rvPCjX4HkY9SbUKOd8V7qw5_r7xRuSOc6GaFt8JoyuQyWQUYNl64AkhZNdMZuiO9uRTk-sAm8qgAM58tcr231SEPqM1wlCTQbsL8IkkpbZH3jnmgCpkYxm2PEnLGp66ULg79r4z8ICQlQIh9FAnmpjOwXp-v1wFB6nYJbDauYdojMuiQyx0nPrIRg53ZsoVFq7BswQVADJNvccXO1YahXf4UXjHbysDOnr6zKyeJSNWjox3zPx9r0xUhcmuHTIJjHsQ"; // Replace `yourJWTToken` with the actual token
const client = new StompJs.Client({
    brokerURL: url,
    connectHeaders: {
        Authorization: token
    }
});

let buttonConnect;
let buttonDisConnect;
let buttonSend;
let greetings;
let nameInput;

client.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

    // Subscribe to the topic for receiving messages
    client.subscribe(topic, (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
};

client.onWebSocketError = (error) => {
    console.error('Error with WebSocket:', error);
};

client.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    buttonConnect.disabled = connected;
    buttonDisConnect.disabled = !connected;
    document.getElementById("conversation").style.display = connected ? "block" : "none";
    greetings.innerHTML = "";
}

function connect() {
    if (!client.active) {
        client.activate();
    }
}

function disconnect() {
    if (client.active) {
        client.deactivate();
    }
    setConnected(false);
}

function sendName() {
    const name = nameInput.value;
    if (client.active && name) {
        client.publish({
            destination: app,
            body: JSON.stringify({ name })
        });
    }
}

function showGreeting(message) {
    const row = document.createElement("tr");
    const cell = document.createElement("td");
    cell.textContent = message;
    row.appendChild(cell);
    greetings.appendChild(row);
}

// Event listener setup after DOM content loads
document.addEventListener("DOMContentLoaded", () => {
    buttonConnect = document.getElementById("connect");
    buttonDisConnect = document.getElementById("disconnect");
    buttonSend = document.getElementById("send");
    greetings = document.getElementById("greetings");
    nameInput = document.getElementById("name");

    buttonConnect.addEventListener("click", (e) => {
        e.preventDefault();
        connect();
    });

    buttonDisConnect.addEventListener("click", (e) => {
        e.preventDefault();
        disconnect();
    });

    buttonSend.addEventListener("click", (e) => {
        e.preventDefault();
        sendName();
    });

    setConnected(false);
});
