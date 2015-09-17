// DEFINE ROUTES
App.Router.map(function() {
	this.route("login", {path: "/"});
	this.route("change_password", {path: "/change_password/"});
	this.resource("launcher", {path: "/launcher/"}, function() {
		this.resource('clouds', { path: '/clouds/' });
		this.resource('configs', { path: '/configs/'});
		this.resource('cloudsteads', { path: '/cloudsteads/' });
		this.resource('addlaunch', {path: '/addlaunch/'}, function() {
			var self = this;
			ADD_LAUNCH_ROUTES.forEach(function(tab) {
				if(tab === "apps"){
					self.resource('apps', function() {
						this.resource('appl', { path: '/app/:app_name' });
					});
				}else{
					self.resource(tab);
				}
			});
		});
		this.resource('add_cloud', {path: '/addcloud/'});
		this.resource('add_cloudstead', {path: '/addcloudstead/'});
	});
});
