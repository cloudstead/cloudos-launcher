<h3 class="centered-title">Create new SHH Key</h3>
<section class="">
	<div class="row centered">
		<form>
			<div class="row">
				Name: {{input type="text" value=name size="50"}}
			</div>
			<div class="row">
				Select a file
			</div>
			<div class="row">
				{{view App.TextFileUpload action="doReadUploadedFile"}}
			</div>
			<div class="row">
				or
			</div>
			<div class="row">
				{{textarea value=publicKey cols="80" rows="6" placeholder="paste key in here"}}
			</div>
			<div class="row">
				<button {{action 'doAddKey'}} class="small-6  medium-4 large-3 columns">Add Key</button>
				<button {{action 'doCancel'}} class="small-6 small-offset-1 medium-4 large-3 columns">Cancel</button>
			</div>
		</form>
	</div>
</section>
