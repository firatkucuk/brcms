<h1>${product.title}</h1>
<a href="../">back</a>

<ul>
  <li><b>Material</b>: ${product.material}</li>
  <#if product.colors??>
  <li><b>Colors</b>:
    <ul>
      <#list product.colors as color>
        <li>${color}</li>
      </#list>
    </ul>
  </li>
  </#if>
  <li><b>Colors</b>: ${product.price} €</li>
</ul>
