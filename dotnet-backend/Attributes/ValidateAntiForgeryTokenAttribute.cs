using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;

namespace QCT.UserManagement.Attributes;

public class ValidateAntiForgeryTokenAttribute : ActionFilterAttribute
{
    public override void OnActionExecuting(ActionExecutingContext context)
    {
        var antiForgeryService = context.HttpContext.RequestServices.GetService<Microsoft.AspNetCore.Antiforgery.IAntiforgery>();
        
        if (antiForgeryService != null)
        {
            try
            {
                antiForgeryService.ValidateRequestAsync(context.HttpContext).Wait();
            }
            catch (Microsoft.AspNetCore.Antiforgery.AntiforgeryValidationException)
            {
                context.Result = new BadRequestObjectResult(new { error = "Invalid anti-forgery token" });
                return;
            }
        }
        
        base.OnActionExecuting(context);
    }
}