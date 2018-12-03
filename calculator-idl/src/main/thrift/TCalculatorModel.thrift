namespace java com.sriram.spring.calculator.dto
namespace py com.sriram.spring.calculator.dto

enum TOperation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}

exception TDivisionByZeroException {

}

exception TAuthException {
    1: optional i16 errorCode
    2: optional string message
}