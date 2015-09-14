ZipGeneratorService = {
	zipFileName: "DATA.zip",

	processorServices: {
		dns: new DNSDataProcessorService(),
		ssl: new SSLDataProcessorService(),
		smtp: new SMTPDataProcessorService(),
		two_factor: new TwoFactorDataProcessorService(),
		storage: new StorageDataProcessorService(),
		geoip: new GeoIPDataProcessorService(),
		claim: new ClaimDataProcessorService(),
		apps: new AppsDataProcessorService(),
		appls: new ApplsDataProcessorService()
	},

	generateFolderStructure: function() {
		var rootFolder = new JSZip();

		var initFilesFolder = rootFolder.folder("init_files");

		var dataBagsFolder = initFilesFolder.folder("data_bags");
		var dataFilesFolder = initFilesFolder.folder("data_files");

		var cloudosFolder = dataBagsFolder.folder("cloudos");

		return {
			root: rootFolder,
			initFiles: initFilesFolder,
			dataBags: dataBagsFolder,
			dataFiles: dataFilesFolder,
			cloudos: cloudosFolder
		};
	},

	generateContentFrom: function(dataToProcess, type) {

		var folders = this.generateFolderStructure();

		var cloudosJSON = INITIAL_CLOUDOS_DATA;

		for (var section in dataToProcess) {
			var outputFolder = null;
			var secondaryData = {};

			switch (section) {
				case "dns":
					outputFolder = folders.dataBags;
					secondaryData = cloudosJSON;
					break;
				case "ssl":
					outputFolder = folders.dataFiles;
					break;
				case "smtp":
					outputFolder = folders.dataBags;
					break;
				case "two_factor":
					outputFolder = folders.dataBags;
					secondaryData = cloudosJSON;
					break;
				case "storage":
					secondaryData = cloudosJSON;
					break;
				case "geoip":
					outputFolder = folders.dataFiles;
					break;
				case "claim":
					secondaryData = cloudosJSON;
					break;
				case "appls":
					outputFolder = folders.dataBags.folder("apps");
					break;
				case "apps":
					break;
			}

		console.log("tab: ", section);

			this.processorServices[section].process(dataToProcess[section], outputFolder, secondaryData);
		}

		folders.cloudos.file("init.json", JSON.stringify(cloudosJSON));

		var content = folders.root.generate({type: type});

		return content;
	},

	generateZipFrom: function(dataToProccess) {
		var content = this.generateContentFrom(dataToProccess, "blob");

		console.log("CONTENT2: ", content);

		// see FileSaver.js
		saveAs(content, this.zipFileName);
	}
};
