DNSDataProcessorService = function() {
	BaseDataProcessorService.call();
};

DNSDataProcessorService.prototype = new BaseDataProcessorService();

DNSDataProcessorService.prototype.process = function(dataToProcess, ouputFolder, extensionData) {
	var dnsData = {
		id: "init",
		admin: {
			name: "cloudos-dns-admin",
			password: "bcrypted-password"
		}
	};

	dnsData = this.extractFields(dataToProcess, dnsData);

	for (var tabGroup in dataToProcess.tabGroups) {
		var tab = dataToProcess.tabGroups[tabGroup].tab;
		if (tab.tabName === "dyndns") {

			dnsData["dyndns"] = {
				account: this.findFieldIn(tab.tabFields, "dns.account").value,
				user: this.findFieldIn(tab.tabFields, "dns.user").value,
				password: this.findFieldIn(tab.tabFields, "dns.password").value,
				zone: this.findFieldIn(tab.tabFields, "dns.zone").value,
			};

		$.extend(extensionData, {
			dns: dnsData["dyndns"]
		});

		} else if (tab.tabName === "djbdns") {
			var cloudosDNSFolder = ouputFolder.folder("cloudos-dns");
			var djbDNSFolder = ouputFolder.folder("djbdns");

			var djbJSON = {
				id: "init",
				allow_axfr: this.findFieldIn(tab.tabFields, "allow_axfr").value,
			};
			dnsData["djbdns"] = true;
			djbDNSFolder.file("init.json", JSON.stringify(djbJSON));

			cloudosDNSFolder.file("init.json", JSON.stringify(dnsData));
		}

	}
};
