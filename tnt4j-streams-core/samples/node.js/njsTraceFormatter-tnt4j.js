var Formatter = require('njstrace/lib/formatter.js'),
    request = require('request');

function Tnt4jStreamsFormatter(args) {
	this.onlyError = args;
}

require('util').inherits(Tnt4jStreamsFormatter, Formatter);


// Implement the onEntry method
Tnt4jStreamsFormatter.prototype.onEntry = function (args) {
	//console.log('Got call TNT4J to %s@%s::%s, num of args: %s, stack location: %s', args.name, args.file, args.line, args.args.length, args.stack.length);
	args.method = 'start';
	args.span = 0;
	if (args.exception == null || args.exception === undefined) {
		args.exception = false;
	}

	if (this.onlyError && !args.exception) {
		// Only exception messages will send 
		return;
	}
    request({
        url: "http://localhost:9596",
        method: "POST",
        json: true,
        headers: {
            "content-type": "application/json",
        },
        body: args
    }, function (error, response, body) {
        if (error && response.statusCode !== 200) {
            console.log("error: " + error)
        }
    });

};

// Implement the onExit method
Tnt4jStreamsFormatter.prototype.onExit = function (args) {
	// console.log('TNT4J Exit from %s@%s::%s, had exception: %s, exit line: %s, execution time: %s, has return value: %s', args.name, args.file, args.line, args.exception, args.retLine, args.span, args.returnValue !== null);
	args.method = "stop";
	if (args.exception == null || args.exception === undefined) {
		args.exception = false;
	}

	if (this.onlyError && !args.exception) {
		return;
	}
	
	if (this.onlyError) {
		//ensure that START is send
		if (args.entryData === undefined) {
			console.log("Error");
		}
		args.entryData.line = args.line;
		args.entryData.exception = "Function timed out";
		__njsTraceEntry__(args.entryData);
	}
	
    request({
        url: "http://localhost:9596",
        method: "POST",
        json: true,
        headers: {
            "content-type": "application/json",
        },
        body: args
    }, function (error, response, body) {
        if (error && response.statusCode !== 200) {
            console.log("error: " + error)
        }
    });

};

module.exports.Tnt4jStreamsFormatter = Tnt4jStreamsFormatter;