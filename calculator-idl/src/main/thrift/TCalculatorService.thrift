include "TCalculatorModel.thrift"

namespace java com.sriram.spring.calculator.api
namespace py com.sriram.spring.calculator.api

typedef i32 int

service TCalculatorService {
    int calculate(1: int i1, 2: int i2, 3: TCalculatorModel.TOperation operation) throws (1: TCalculatorModel.TDivisionByZeroException dze, 2: TCalculatorModel.TAuthException tae)
}