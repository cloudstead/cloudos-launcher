<h3 class="centered-title">
	{{t launch_cloudstead.title}} {{cloudOs.name}}
</h3>
<section class="">
	<div class="row center progress_report">
		<p>{{lastMessage}} {{percent_value}}%</p>
		<progress {{bindAttr value=percent_value}} max="100"></progress>
	</div>

	<div class="row">
		{{#link-to "cloudsteads" classNames="button button-small"}}{{t launch_cloudstead.close}}{{/link-to}}
	</div>
</section>
