Validator = {
	ValidateField: function(element){
		var elem = $(element);
		//Wconsole.log("field to validate: ", elem, elem.val());
		var kind = Ember.isNone(elem.attr("data-kind")) ? elem.attr("data-kindtab") : elem.attr("data-kind");
		var required = elem.attr("data-required") === "true";
		var errorData = { isValid: true };

		switch(kind){
			case "email":
				//errorData = EmailValidator.getErrors(elem.val(), required);
				errorData = PatternValidator.getErrors(elem.val(), required, this.EmailPattern);
				break;
			case "fqdn":
				errorData = PatternValidator.getErrors(elem.val(), required, this.FqdnPattern);
				break;
			case "host":
				errorData = PatternValidator.getErrors(elem.val(), required, this.IPPattern);
				if(!result.isValid){
					errorData = PatternValidator.getErrors(elem.val(), required, this.URLPattern);
				}
				break;
			case "port":
				errorData = PatternValidator.getErrors(elem.val(), required, this.NumberPattern);
				break;
			case "list_ipaddr":
				errorData = IPListValidator.getErrors(elem.val(), required);
				break;
			case "cron":
				errorData = PatternValidator.getErrors(elem.val(), required, this.CronPattern);
				break;
			case "username":
			case "login":
			case "secret_key":
			case "password":
				errorData = PatternValidator.getErrors(elem.val(), required, this.StartWithLetterPattern);
				break;
			case "hash":
			case "hostname_part":
			case "hostname":
			case "field":
			case "text":
				errorData.isValid = RequiredValidator.isValid(elem.val(), required);
				if(!errorData.isValid){ errorData.message = "Field is required"; }
				break;
			default:
				console.log("===");
				break;
		}

		if(errorData.isValid){ return true;}
		elem.notify( errorData.message, this.NotifyOptions );
		return false;
	},

	ElementsForValidation: function(){
		var elemOnActiveTab = this.ElementsOnActiveTab();
		var elements = $("[data-kind]");
		$.merge(elements, elemOnActiveTab);

		return elements;
	},

	ElementsOnActiveTab: function(){
		var activeLi = $("li.active")[0];
		var elemOnActiveTab = $("#" + $(activeLi).attr("id") +"-tab").find("[data-kindtab]");
		return elemOnActiveTab;
	},

	EmailPattern: /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
	FqdnPattern: /^(?=.{1,254}$)((?=[a-z0-9-]{1,63}\.)(xn--+)?[a-z0-9]+(-[a-z0-9]+)*\.)+[a-z]{2,63}$/i,
	IPPattern: /^(([1-9]?\d|1\d\d|2[0-5][0-5]|2[0-4]\d)\.){3}([1-9]?\d|1\d\d|2[0-5][0-5]|2[0-4]\d)$/,
	URLPattern: /^(([1-9]?\d|1\d\d|2[0-5][0-5]|2[0-4]\d)\.){3}([1-9]?\d|1\d\d|2[0-5][0-5]|2[0-4]\d)$/,
	NumberPattern: /^\d*$/,
	StartWithLetterPattern: /^[A-Za-z][A-Za-z0-9]*(?:_[A-Za-z0-9]+)*$/,
	CronPattern: /(28|\*) (2|\*) (7|\*) (1|\*) (1|\*)/,
	NotifyOptions: { position:"bottom",autoHideDelay: 1800 }
};

EmptyValidator = {
	isEmpty: function(value) {
		return (Ember.isNone(value) || String(value).trim().length === 0);
	}
};

RequiredValidator = {
	isValid: function(value, required){
		if(required && EmptyValidator.isEmpty(value)){
			return false;
		}
		return true;
	}
};

PatternValidator = {
	getErrors: function(value, required, pattern) {
		var error = { isValid: false, message: "" };

		if(!RequiredValidator.isValid(value, required)){
			error["message"] = "Field is required";
		}else if (!pattern.test(value)){
			error["message"] = "Data is not valid";
		}else{
			error["isValid"] = true;
		}
		return error;
	}
};

IPListValidator = {
	getErrors:function(host, required) {
		var error = { isValid: false, message: "" };
		var pattern = /^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$/;
		var tokens = host.split(/\s*,\s*/);
		var i;
		for (i = 0; i < tokens.length; ++i) {
			if (tokens[i] !== "" && !pattern.test(tokens[i])) {
				error.isValid = false;
				error.message = "Not valid. Use format: xxx.xxx.xxx.xxx, where xxx is 255 maximum";
			}
		}
		return error;
	}
};
