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
	createNew: function(subTabName, parentName, model, translation) {

		var fieldsArray = App.FieldModel.createFields(model, translation);
		var fileFieldsArray = App.FieldModel.createFileFields(model, translation);

		var a = App.SubTabModel.create({
			tabName: subTabName,
			tabContentId: subTabName + '-tab',
			tabParent: parentName,
			tabData: model[subTabName],
			tabFields: fieldsArray,
			tabFileFields: fileFieldsArray,
			tabGroup: model["exclusive"],
			tabDescription: Ember.String.htmlSafe(translation['description']),
			isActive: false,
		});

		return a;
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
