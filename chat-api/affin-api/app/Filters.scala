import javax.inject.Inject

import com.affin.chat.util.{FailureFilter, LoggingFilter}
import play.api.http.HttpFilters

class Filters @Inject()(
                         failureFilter: FailureFilter,
                         loggingFilter: LoggingFilter
                       ) extends HttpFilters {

  val filters = Seq(loggingFilter, failureFilter)
}