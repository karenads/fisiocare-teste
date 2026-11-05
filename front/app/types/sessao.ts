export class Sessao {
    constructor(
        public id: number | null,
        public data: string,
        public horario: string,
        public descricao: string,
        public observacoes: string,
        public realizada: boolean,
        public status: "ATIVO" | "EXCLUIDO",
        public tratamentoId: number | null = null
    ) {}
}
export interface SessaoFormProps {
    sessaoExistente?: Sessao;
}
