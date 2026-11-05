export class Tratamento {
    constructor(
        public id: number | null,
        public nome: string,
        public descricao: string,
        public dataInicio: string,
        public dataFinal: string,
        public totalSessoes: number,
        public sessoesRealizadas: number,
        public status: "ATIVO" | "EXCLUIDO",
        public pacienteId: number | null = null
    ) {}
}
export interface TratamentoFormProps {
    tratamentoExistente?: Tratamento;
}
