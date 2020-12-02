package gannaSquareAPI.`interface`

import gannaSquareAPI.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ExceptionHandlers {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Throwable::class)
    @ResponseBody
    fun handleThrowable(ex: Throwable?): ErrorResponse? {
        return ErrorResponse(ex?.message ?: ex?.toString())
    }
}

data class ErrorResponse(val errorMesage: String?)