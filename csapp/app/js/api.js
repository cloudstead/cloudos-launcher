API = {
	get_providers: function() {
		var response = [];
		for(i = 0; i< 5; i++){
			response.push({
				id: i,
				name: "server" + i,
				provider: "provider" + i,
			});
		}
		return response;
	},

	delete_provider: function() {

	},
};
