App.SubTabModel = Ember.Object.extend({
	serialize: function () {
		return {
			tabName: this.get("tabName"),
			tabContentId: this.get("tabContentId"),
			tabParent: this.get("tabParent"),
			tabData: this.get("tabData"),
			tabFields: App.FieldModel.serializeArray(this.get("tabFields")),
			tabFileFields: App.FieldModel.serializeArray(this.get("tabFileFields")),
			tabDescription: this.get("tabDescription"),
			isActive: this.get("isActive"),
			isExclusive: this.get("isExclusive"),
			tabGroup: this.get("tabGroup"),
		};
	},

	dataObserver: function() {

		if (this.get("isActive")) {
			$("#"+this.get("tabContentId")).fadeIn();
			$("#"+this.get("tabName")).addClass("active");

			// ON EVENTS ON NEW ACTIVE TAB
			$.each(Validator.ElementsOnActiveTab(), function(index, elem){
				$(elem).bind("focusout", function(e) {
					Validator.ValidateField(e.target);
				});
			});
		} else {
			$("#"+this.get("tabName")).removeClass("active");
			$("#"+this.get("tabContentId")).css("display", "none");
		}

		if (Ember.isNone(DATA[this.get("tabParent")])) {
			DATA[this.get("tabParent")] = {
				tabGroups: {}
			};
		} else if (Ember.isNone(DATA[this.get("tabParent")].tabGroups)) {
			DATA[this.get("tabParent")].tabGroups = {};
		}

		DATA[this.get("tabParent")]["tabGroups"][this.get("tabData.exclusive")] = {
			tab: this
		};

		var fields = DATA[this.get("tabParent")]["tabGroups"][this.get("tabData.exclusive")].tab.tabFields;

		fields.forEach(function(field, index){
			if (field.get("type.typeName") === 'file') {
				readURL($("#"+field.get("elementId"))[0], fields, index);
			}
		});
	}.observes("isActive","tabFields.@each.value"),

	isExclusive: function() {
		return !Ember.isNone(this.get("tabData.exclusive"));
	}.property(),

	tabGroup: function() {
		return this.get("isExclusive") ? this.get("tabData.exclusive") : GENERAL_SUBTAB_GROUP;
	}.property("isExclusive"),
});

App.SubTabModel.reopenClass({
	createNew: function(subTabName, subTabData, groupName, parentName, translation, data, appName) {

		var fieldsArray = App.FieldModel.createSubTabFields(subTabName, subTabData, translation, data, appName);
		// var fieldsArray = App.FieldModel.createFields(model, translation);
		var fileFieldsArray = []; //App.FieldModel.createFileFields(model, translation);

		var a = App.SubTabModel.create({
			tabName: subTabName,
			tabContentId: subTabName + '-tab',
			tabParent: parentName,
			tabData: subTabData,
			tabFields: fieldsArray,
			tabFileFields: fileFieldsArray,
			tabGroup: groupName,
			tabTitle: Ember.String.htmlSafe(translation['label']),
			tabDescription: Ember.String.htmlSafe(translation['info']),
			tabHelp: Ember.String.htmlSafe(translation['help']),
			isActive: false,
		});

		return a;
	},

	// createNew: function(subTabName, parentName, model, translation) {

	// 	var fieldsArray = App.FieldModel.createFields(model, translation);
	// 	var fileFieldsArray = App.FieldModel.createFileFields(model, translation);

	// 	var a = App.SubTabModel.create({
	// 		tabName: subTabName,
	// 		tabContentId: subTabName + '-tab',
	// 		tabParent: parentName,
	// 		tabData: model[subTabName],
	// 		tabFields: fieldsArray,
	// 		tabFileFields: fileFieldsArray,
	// 		tabGroup: model["exclusive"],
	// 		tabDescription: Ember.String.htmlSafe(translation['description']),
	// 		isActive: false,
	// 	});

	// 	return a;
	// },

	createArrayUsingDataFrom: function (data, parentName) {
		var subTabData = {};
		var subTabArray = [];
		var allData = [];

		var dataPath = data.split("/");
		var appName = dataPath[0];
		var category = dataPath[1];
		var field = dataPath[2];

		var jsonURL = APPS_DATA_PATH + appName + "/" + METADATA_FILENAME;
		var translationURL = APPS_DATA_PATH + appName + "/" + TRANSLATION_FILENAME;

		$.ajax({
				dataType: "json",
				url: jsonURL,
				async: false,
				success: function (data) {
					allData = data;
					subTabData = data["categories"][category]["fields"][field];
				}
			});

		$.ajax({
			dataType: "json",
			url: translationURL,
			async: false,
			success: function (data) {
				translation = data["categories"][category][field];
			}
		});
			console.log("allData: ", allData);

		var choices = subTabData["choices"];
		var choicesData = subTabData["sub_fields"];
		var choicesTrans = translation["choices"];
		var groupName = subTabData["group"];

		choices.forEach(function(choice){
			var transForChoice = choicesTrans.find(function(transl){
				return transl["label"] === choice;
			});
			var tabData = choicesData.find(function(choiceData){
				return choiceData["label"] === choice;
			});

			subTabArray.push(App.SubTabModel.createNew(choice, choicesData, field, parentName, transForChoice, allData, appName));
		});

		return subTabArray;

	},

	createArrayUsingListOfNames: function(subTabList, parentName, model, translation) {
		var subTabs = [];

		subTabList.forEach(function(subTab){
			subTabs.push(
				App.SubTabModel.createNew(subTab, parentName, model[subTab], translation[subTab])
			);
		});

		return subTabs;
	},

	serializeArray: function (subTabModels) {
		var retArray = [];
		subTabModels.forEach(function(subTabModel) {
			retArray.push(subTabModel.serialize());
		});
		return retArray;
	}
});
