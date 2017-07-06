import {Pipe, PipeTransform} from '@angular/core'

@Pipe({name: 'orderBy', pure: false})
export class OrderBy implements PipeTransform {

  static _orderByComparator(a: any, b: any): number {

    if ((isNaN(parseFloat(a)) || !isFinite(a)) || (isNaN(parseFloat(b)) || !isFinite(b))) {
      if (a.toLowerCase() < b.toLowerCase()) return -1
      if (a.toLowerCase() > b.toLowerCase()) return 1
    }
    else {
      if (parseFloat(a) < parseFloat(b)) return -1
      if (parseFloat(a) > parseFloat(b)) return 1
    }

    return 0
  }

  transform(input: any, [config = '+']): any {

    if (!Array.isArray(input)) return input

    if (!Array.isArray(config) || (Array.isArray(config) && config.length == 1)) {
      let propertyToCheck: string = !Array.isArray(config) ? config : config[0]
      let desc = propertyToCheck.substr(0, 1) == '-'

      if (!propertyToCheck || propertyToCheck == '-' || propertyToCheck == '+') {
        return !desc ? input.sort() : input.sort().reverse()
      }
      else {
        let property: string = propertyToCheck.substr(0, 1) == '+' || propertyToCheck.substr(0, 1) == '-'
          ? propertyToCheck.substr(1)
          : propertyToCheck

        return input.sort(function (a: any, b: any) {
          return !desc
            ? OrderBy._orderByComparator(a[property], b[property])
            : -OrderBy._orderByComparator(a[property], b[property])
        })
      }
    }
    else {
      return input.sort(function (a: any, b: any) {
        for (let i: number = 0; i < config.length; i++) {
          let desc = config[i].substr(0, 1) == '-'
          let property = config[i].substr(0, 1) == '+' || config[i].substr(0, 1) == '-'
            ? config[i].substr(1)
            : config[i]

          let comparison = !desc
            ? OrderBy._orderByComparator(a[property], b[property])
            : -OrderBy._orderByComparator(a[property], b[property])

          if (comparison != 0) return comparison
        }

        return 0
      })
    }
  }
}