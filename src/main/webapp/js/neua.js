const NeuAWebSocket = Object.freeze(function () {

	let debug = false;

	let webSocket = null;
	let count = 0;

	const url = "ws://127.0.0.1:8080/jk17y/";

	const createWebSocket = () => {
		console.log("neua " + url);

		// WebSocket已存在，不做任何事
		if (isObject(webSocket)) {
			console.log("neua connection exists, do nothing");
			return;
		}

		let tempWebSocket = null;
		if ("WebSocket" in window) {
			tempWebSocket = new WebSocket(url);
		} else {
			// 不支援WebSocket，不做任何事
			console.log("neua webSocket is not supported");
			return;
		}

		tempWebSocket.onerror = () => {
			console.warn("neua webSocket error");
			if (isObject(webSocket)) {
				webSocket.close();
			}
			webSocket = null;
		};

		tempWebSocket.onopen = () => {
			console.log("neua webSocket connected");
		};

		tempWebSocket.onmessage = (event) => {
			count = 0;
			var data = JSON.parse(event.data);
			switch (data.msg) {
				case "GetDomainSetting" :
					bestLineTest.receive(data.obj);
					break;
				case "GetHlsConfigSetting" :
					heartCheck.start();
					break;
			}
		};

		tempWebSocket.onclose = () => {
			console.log("neua webSocket closed");
			if (isObject(webSocket)) {
				webSocket.close();
			}
			webSocket = null;
		};

		webSocket = tempWebSocket;

		window.onbeforeunload = () => {
			try {
				heartCheck.close();
				webSocket.close();
			} catch (e) {
				// nothing to be done
			}
		};
	};

	const heartCheck = {
		hearbeatTask: null,
		hearbeatContent: "{\"router\":\"heartbeat\"}",
		start() {
			if (isObject(this.hearbeatTask)) {
				clearInterval(this.hearbeatTask);
				this.hearbeatTask = null;
			}
			this.hearbeatTask = setInterval(() => {
				if (debug)
					console.log("neua heartbeat:" + new Date());
				try {
					count += 1;
					if (webSocket === null) {
						count = 0;
						createWebSocket();
						return;
					}
					if (count >= 3) {
						count = 0;
						if (isObject(webSocket)) {
							webSocket.close();
						}
						webSocket = null;
						return;
					}
					if (isObject(webSocket)) {
						webSocket.send(this.hearbeatContent);
					}
				} catch (e) {
					// nothing to be done
				}
			}, config.heartbeat_timeout);
		},
		close() {
			if (isObject(this.hearbeatTask)) {
				clearInterval(this.hearbeatTask);
				this.hearbeatTask = null;
			}
		}
	};

	const sendMsg = (msg) => {
		if (!isObject(webSocket)) {
			console.log("webSocket doesn't exist, do nothing");
			return false;
		}
		if (debug){
			console.log("sned msg: " + msg);
		}
		webSocket.send(msg);
		return true;
	};

	const isObject = (obj) => {
		return obj !== null && typeof obj !== "undefined";
	};

	const connect = () => {
		createWebSocket();
	};

	const init = () => {
		$j("#send").on("click", () => {
			const testMsg = $j("#testMsg").val();
			console.info("testMsg >> " + testMsg);
			sendMsg(testMsg);
		});
	};

	return {
		connect,
		init
	};
}());
