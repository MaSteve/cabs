package com.raincoatmoon.Nodes.Functions;

import com.raincoatmoon.Nodes.Declaration;
import com.raincoatmoon.Nodes.TypeNode;
import com.raincoatmoon.Nodes.Variables.DeclarationNode;
import com.raincoatmoon.Nodes.Variables.StaticArrayNode;
import com.raincoatmoon.Nodes.Variables.VarIDNode;

public class ArgumentNode extends DeclarationNode {

    public ArgumentNode(TypeNode type, VarIDNode id) {
        super(type, id);
        typeClass = Declaration.ARG;
    }

    public ArgumentNode(TypeNode type, VarIDNode id, StaticArrayNode arrayNode) {
        super(type, id, arrayNode);
        typeClass = Declaration.ARG;
    }
}
