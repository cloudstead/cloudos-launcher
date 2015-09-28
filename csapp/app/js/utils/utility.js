// HELPERS
String.prototype.toCamel = function () {
	return this.replace(/\W+(.)/g, function (x, chr) {
		return chr.toUpperCase();
	}).replace(/_+(.)/g, function (x, chr) {
		return chr.toUpperCase();
	});
};

String.prototype.ToRoute = function () {
	var c = this.toCamel();
	return c.charAt(0).toUpperCase() + c.slice(1) + 'Route';
};

String.prototype.toController = function () {
	var c = this.toCamel();
	return c.charAt(0).toUpperCase() + c.slice(1) + 'Controller';
};

var getModels = function(routeName, params){
	var data = {}; data.apps = [];
	if(additional_routes.indexOf(routeName) > -1){
		if(routeName === "apps"){

		}
	}else{
		data = INIT_CONFIG[routeName];
	}

	data.routeName = routeName;
	return data;
};

var readURL = function(input, dataArray, dataIndex) {
	if (input.files && input.files[0]) {
		var reader = new FileReader();

		reader.onload = function (e) {
			dataArray[dataIndex].set("fileData", e.target.result);
		};

		reader.readAsDataURL(input.files[0]);
	}
};

var getFirstTranslation = function() {
	var lang = window.navigator.languages.find(function(l) {
		return !Ember.isNone(TRANSLATIONS[l]);
	});

	console.log("LANG: ", lang, window.navigator.languages);

	Ember.I18n.translations = Ember.isNone(lang) ? TRANSLATIONS['en'] : TRANSLATIONS[lang];

	return Ember.I18n.translations;
};

var getValues = function(appName, dataKind, fieldType){
	var r = "", key = "appls";
	if(DATA[key] === undefined || DATA[key][appName] === undefined || DATA[key][appName][fieldType] === undefined){
		return r;
	}

	for (var f in DATA[key][appName][fieldType]) {
		if(!Ember.isNone(DATA[key][appName][fieldType][f]) && DATA[key][appName][fieldType][f]["dataKind"] === dataKind){
			r = DATA[key][appName][fieldType][f]["value"];
		}
	}
	return r;
};
