package gannaSquareAPI.domain.gannSquare

import gannaSquareAPI.Common.getLogger
import org.springframework.stereotype.Service

interface QannSquareService {

}

@Service
class QannSquareServiceImpl : QannSquareService {
    companion object {
        private val loggerWithExplicitClass
                = getLogger(QannSquareServiceImpl::class.java)
    }
}