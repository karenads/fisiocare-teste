"use client";

import { Paciente, PacienteFormProps } from "@/app/types/paciente";
import { salvarPaciente, atualizarPaciente, buscarCep } from "@/app/services/pacienteService";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { useState } from "react";

export default function PacienteForm({ pacienteExistente }: PacienteFormProps) {
    const router = useRouter();
    const [cep, setCep] = useState("");

    const consultarCep = async () => {
        try {
            const endereco = await buscarCep(cep);
            if (endereco.erro) {
                alert("CEP não encontrado!");
                return;
            }
            setPaciente(anterior => ({
                ...anterior,
                endereco: endereco.logradouro + ", " + endereco.bairro + ", "
                    + endereco.localidade + " - " + endereco.uf
            }));
        } catch (error) {
            alert("Erro ao consultar ViaCEP!");
        }
    };
    const [paciente, setPaciente] = useState<Paciente>(
        pacienteExistente || new Paciente(null, "", "", "", "", "", "", "", "ATIVO")
    );

    const alterar = (campo: keyof Paciente, valor: string) => {
        setPaciente({ ...paciente, [campo]: valor });
    };

    const salvar = async (formData: FormData) => {
        try {
            if (pacienteExistente) {
                await atualizarPaciente(paciente);
            } else {
                await salvarPaciente(paciente);
            }
            alert("Paciente salvo com sucesso!");
            router.push("/pacientes");
        } catch (error) {
            alert("Erro ao salvar paciente! Verifique os dados.");
        }
    };

    return (
        <form action={salvar} className="space-y-6">
            <div className="mb-6">
                <label className="block mb-2 text-purple-800">Consultar CEP (ViaCEP)</label>
                <div className="flex gap-2">
                    <input value={cep} onChange={e => setCep(e.target.value)}
                        placeholder="88800000" maxLength={9}
                        className="rounded-lg border border-purple-200 p-3 text-gray-800" />
                    <button type="button" onClick={consultarCep}
                        className="rounded-lg bg-purple-700 px-4 py-2 text-white">
                        Buscar endereço
                    </button>
                </div>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {([
                    ["nome", "Nome", "text"],
                    ["cpf", "CPF", "text"],
                    ["telefone", "Telefone", "text"],
                    ["email", "E-mail", "email"],
                    ["dataNascimento", "Data de nascimento", "date"],
                    ["endereco", "Endereço", "text"],
                    ["observacoes", "Observações", "text"]
                ] as const).map(([campo, titulo, tipo]) => (
                    <div key={campo}>
                        <label className="block mb-2 font-medium text-purple-800">{titulo}</label>
                        <input name={campo} type={tipo}
                            required={campo !== "observacoes" && campo !== "endereco"}
                            value={paciente[campo]}
                            onChange={e => alterar(campo, e.target.value)}
                            className="w-full rounded-lg border border-purple-200 p-3 text-gray-800" />
                    </div>
                ))}
            </div>
            <div className="flex justify-end gap-4">
                <Link href="/pacientes" className="rounded-lg bg-purple-100 px-5 py-3 text-purple-800">Cancelar</Link>
                <button type="submit" className="rounded-lg bg-purple-700 px-5 py-3 text-white">Salvar</button>
            </div>
        </form>
    );
}
