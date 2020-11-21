package gannaSquareAPI.`interface`

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/gannaSqure")
@Api(value = "GannaSquare", description = "Ganna Square API", tags = arrayOf("Ganna Square API"))
class GannaSquareController {
    @RequestMapping(
            value = ["/of/{price}/with/{digit}/sensitivity"],
            method = arrayOf(RequestMethod.GET),
            produces = arrayOf("application/json")
    )
    @ApiOperation(value = "ganna square guessing", response = String::class)
    @ApiResponses(
            value = *arrayOf(
                    ApiResponse(code = 200, message = "OK"),
                    ApiResponse(code = 401, message = "You are not authorized access the resource"),
                    ApiResponse(code = 404, message = "The resource not found")
            )
    )
    fun message(@PathVariable price: BigDecimal, @PathVariable digit : BigDecimal): String {
        return "${price}-${digit}"
    }

}