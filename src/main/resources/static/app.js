/**
 * Created by raczynsd on 2017-02-03.
 */
var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function personalConnect() {
    var socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setPersonalConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/twoajastara', function (greeting) {
            showPersonalGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function personalDisconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setPersonalConnected(false);
    console.log("Disconnected");
}

function setPersonalConnected(connected) {
    $("#personal-connect").prop("disabled", connected);
    $("#personal-disconnect").prop("disabled", !connected);
    if (connected) {
        $("#personal-conversation").show();
    }
    else {
        $("#personal-conversation").hide();
    }
    $("#personal-greetings").html("");
}

function personalSend() {
    stompClient.send("/app/personal", {}, JSON.stringify({'name': $("#personal-name").val()}));
}

function showPersonalGreeting(message) {
    $("#personal-greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
    $("#personal-connect").click(function () {
        personalConnect();
    });
    $("#personal-disconnect").click(function () {
        personalDisconnect();
    });
    $("#personal-send").click(function () {
        personalSend();
    });
});