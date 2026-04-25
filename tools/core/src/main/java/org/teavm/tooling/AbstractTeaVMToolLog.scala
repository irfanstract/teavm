
package org.teavm.tooling




import language.unsafeNulls

abstract class AbstractWithStacktraceTeaVMToolLog extends TeaVMToolLog
{

    override def info   (text: String, e: Throwable ): Unit = {}
    override def debug  (text: String, e: Throwable ): Unit = {}
    override def warning(text: String, e: Throwable ): Unit = {}
    override def error  (text: String, e: Throwable ): Unit = {}

    override def    info(text: String): Unit = info(text, null )
    override def   debug(text: String): Unit = debug(text, null )
    override def warning(text: String): Unit = warning(text, null )
    override def   error(text: String): Unit = error(text, null )

}

// abstract class AbstractDiscardedStacktraceTeaVMToolLog extends TeaVMToolLog
// {

//     // @Override
//     // public void info(String text) {
//     // }
//     override def info(text: String): Unit = {}

//     // @Override
//     // public void debug(String text) {
//     // }
//     override def debug(text: String): Unit = {}

//     // @Override
//     // public void warning(String text) {
//     // }
//     override def warning(text: String): Unit = {}

//     // @Override
//     // public void error(String text) {
//     // }
//     override def error(text: String): Unit = {}

// }


