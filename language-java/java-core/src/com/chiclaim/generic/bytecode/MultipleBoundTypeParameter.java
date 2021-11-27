package com.chiclaim.generic.bytecode;

import com.chiclaim.generic.BoundedTypeParameter;

import java.io.Serializable;

abstract class MultipleBoundTypeParameter<Type extends BoundedTypeParameter.ClassBound & Serializable> extends Number{

}