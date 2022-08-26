<h1>${section.title}</h1>
<p>${section.content}</p>
<a href="../">back</a>

<ul>
  <#list section.subPages as page>
    <li><a href="${page.url}/">${page.title}</a></li>
  </#list>
</ul>
